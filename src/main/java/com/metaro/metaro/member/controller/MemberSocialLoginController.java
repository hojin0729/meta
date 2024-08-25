package com.metaro.metaro.member.controller;

import com.metaro.metaro._core.utils.ApiUtils;
import com.metaro.metaro.member.dto.MemberResponseDTO;
import com.metaro.metaro.member.service.MemberSocialLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "카카오 회원 API", description = "소셜 로그인 및 회원가입 기능을 제공하는 API")
public class MemberSocialLoginController {

    private final MemberSocialLoginService memberSocialLoginService;

    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=0a557f7be997372534d371890da6767a&redirect_uri=http://localhost:8080/api/auth/kakao/login
    /*
        카카오 로그인
     */
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestParam(name = "code") String code) {
        MemberResponseDTO.authTokenDTO responseDTO = memberSocialLoginService.kakaoLogin(code);
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }


}
