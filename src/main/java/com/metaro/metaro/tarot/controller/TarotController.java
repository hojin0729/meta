package com.metaro.metaro.tarot.controller;

import com.metaro.metaro.member.domain.Member;
import com.metaro.metaro.tarot.dto.TarotRequestDTO;
import com.metaro.metaro.tarot.domain.TarotResponse;
import com.metaro.metaro.tarot.service.TarotResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tarot")
@Tag(name = "타로 API", description = "타로 리딩 제출 및 히스토리 불러오기 기능을 제공하는 API")
public class TarotController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final TarotResponseService tarotResponseService;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    @Autowired
    public TarotController(TarotResponseService tarotResponseService) {
        this.tarotResponseService = tarotResponseService;
    }

    @PostMapping("/submit")
    @Operation(summary = "타로 리딩 제출", description = "Python 서버로 타로 리딩을 제출하고 응답을 유니티 클라이언트에 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "타로 리딩 결과가 성공적으로 반환되었습니다."),
            @ApiResponse(responseCode = "500", description = "Python 서버와의 통신에 실패했습니다.")
    })
    public ResponseEntity<?> submitTarotReading(@RequestBody TarotRequestDTO requestDTO, @AuthenticationPrincipal Member member) {
        // Python 서버로 요청을 전송하기 위해 JSON 데이터 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TarotRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

        // Python 서버로 요청 전송
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(pythonServerUrl + "/chatbot/chat-bot", requestEntity, String.class);
        } catch (RestClientException e) {
            log.error("Python 서버와의 통신에 실패했습니다. 상세 정보: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Python 서버와의 통신에 실패했습니다.");
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode()).body("Python 서버로부터의 응답 오류입니다.");
        }

        // AI 서버의 응답을 저장
        String responseBody = response.getBody();
        if (responseBody == null || responseBody.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("유효하지 않은 응답을 받았습니다.");
        }

        tarotResponseService.saveResponse(responseBody, member);  // 회원 정보와 함께 저장

        // 클라이언트(유니티)에게 AI 서버의 응답을 반환
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/history")
    @Operation(summary = "타로 리딩 히스토리 가져오기", description = "로그인한 회원의 모든 타로 리딩 결과를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 타로 리딩 히스토리를 반환했습니다."),
            @ApiResponse(responseCode = "404", description = "타로 리딩 히스토리를 찾을 수 없습니다.")
    })
    public ResponseEntity<?> getTarotHistory(@AuthenticationPrincipal Member member) {
        List<TarotResponse> history = tarotResponseService.getAllResponsesByMember(member);

        if (history.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("타로 리딩 히스토리를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(history);
    }

}