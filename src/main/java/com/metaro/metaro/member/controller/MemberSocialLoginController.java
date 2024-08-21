package com.metaro.metaro.member.controller;

import com.metaro.metaro._core.utils.ApiUtils;
import com.metaro.metaro.member.dto.MemberResponseDTO;
import com.metaro.metaro.member.service.MemberSocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class MemberSocialLoginController {

    private final MemberSocialLoginService memberSocialLoginService;

    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=262c56061ee06d4004d2f9b94db133a4&redirect_uri=http://localhost:8080/api/auth/kakao/login
    /*
        카카오 로그인
     */
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestParam(name = "code") String code) {
        System.out.println("code: " + code);
        MemberResponseDTO.authTokenDTO responseDTO = memberSocialLoginService.kakaoLogin(code);
        System.out.println("ResponseDTO : " + responseDTO);
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

  
}
