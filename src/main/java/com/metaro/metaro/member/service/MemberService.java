package com.metaro.metaro.member.service;

import com.metaro.metaro._core.error.ApplicationException;
import com.metaro.metaro._core.error.ErrorCode;
import com.metaro.metaro._core.jwt.JWTTokenProvider;
import com.metaro.metaro._core.utils.ClientUtils;
import com.metaro.metaro.member.domain.*;
import com.metaro.metaro.member.dto.MemberRequestDTO;
import com.metaro.metaro.member.dto.MemberResponseDTO;
import com.metaro.metaro.member.repository.MemberRepository;
import com.metaro.metaro.redis.domain.RefreshToken;
import com.metaro.metaro.redis.repository.RefreshTokenRedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JWTTokenProvider jwtTokenProvider;

    private final String EMAIL_PREFIX = "email:";
    private final long EMAIL_VALIDATATION_TIME = 7L;
    private final String VALIDATED_EMAIL_STATUS = "true";

    /*
        기본 회원 가입
     */
    @Transactional
    public void signUp(MemberRequestDTO.signUpDTO requestDTO) {



        // 비밀번호 확인
        checkValidPassword(requestDTO.password(), passwordEncoder.encode(requestDTO.confirmPassword()));

        // 회원 생성
        Member member = newMember(requestDTO);

        // 회원 저장
        memberRepository.save(member);

    }

    /*
        기본 로그인
     */
    public MemberResponseDTO.authTokenDTO login(HttpServletRequest httpServletRequest, MemberRequestDTO.loginDTO requestDTO) {

        // 1. 이메일 확인
        Member member = findMemberByEmail(requestDTO.email())
                .orElseThrow(() -> new ApplicationException(ErrorCode.EMPTY_EMAIL_MEMBER));

        // 2. 비밀번호 확인
        checkValidPassword(requestDTO.password(), member.getPassword());

        return getAuthTokenDTO(requestDTO.email(), requestDTO.password(), httpServletRequest);
    }

    // 비밀번호 확인
    private void checkValidPassword(String rawPassword, String encodedPassword) {

        log.info("{} {}", rawPassword, encodedPassword);

        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
    }

    protected Optional<Member> findMemberByEmail(String email) {
        log.info("회원 확인 : {}", email);

        return memberRepository.findByEmail(email);
    }

    // 회원 생성
    protected Member newMember(MemberRequestDTO.signUpDTO requestDTO) {
        return Member.builder()
                .name(requestDTO.name())
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .gender(Gender.fromString(requestDTO.gender()))
                .socialType(SocialType.NONE)
                .authority(Authority.USER)
                .build();
    }

    // 토큰 발급
    protected MemberResponseDTO.authTokenDTO getAuthTokenDTO(String email, String password, HttpServletRequest httpServletRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);
        AuthenticationManager manager = authenticationManagerBuilder.getObject();
        Authentication authentication = manager.authenticate(usernamePasswordAuthenticationToken);

        MemberResponseDTO.authTokenDTO authTokenDTO = jwtTokenProvider.generateToken(authentication);

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .ip(ClientUtils.getClientIp(httpServletRequest))
                .authorities(authentication.getAuthorities())
                .refreshToken(authTokenDTO.refreshToken())
                .build()
        );

        return authTokenDTO;
    }

    // 토큰 재발급
    public MemberResponseDTO.authTokenDTO reissueToken(HttpServletRequest httpServletRequest) {

        // Request Header 에서 JWT Token 추출
        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        // 토큰 유효성 검사
        if(token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ApplicationException(ErrorCode.FAILED_VALIDATE_ACCESS_TOKEN);
        }

        // type 확인
        if(!jwtTokenProvider.isRefreshToken(token)) {
            throw new ApplicationException(ErrorCode.IS_NOT_REFRESH_TOKEN);
        }

        // RefreshToken
        RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);

        if(refreshToken == null) {
            throw new ApplicationException(ErrorCode.FAILED_GET_RERFRESH_TOKEN);
        }

        // 최초 로그인한 ip와 같은지 확인
        String currentIp = ClientUtils.getClientIp(httpServletRequest);
        if(!currentIp.equals(refreshToken.getIp())) {
            throw new ApplicationException(ErrorCode.DIFFERENT_IP_ADDRESS);
        }

        // Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
        MemberResponseDTO.authTokenDTO authTokenDTO = jwtTokenProvider.generateToken(
                refreshToken.getId(), refreshToken.getAuthorities()
        );

        // Redis 에 RefreshToken Update
        refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(refreshToken.getId())
                        .ip("")
                        .authorities(refreshToken.getAuthorities())
                        .refreshToken(authTokenDTO.refreshToken())
                .build());

        return authTokenDTO;
    }

    /*
        로그아웃
     */
    public void logout(HttpServletRequest httpServletRequest) {
        
        log.info("로그아웃 - Refresh Token 확인");

        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if(token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ApplicationException(ErrorCode.FAILED_VALIDATE__REFRESH_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
        refreshTokenRedisRepository.delete(refreshToken);
    }
}
