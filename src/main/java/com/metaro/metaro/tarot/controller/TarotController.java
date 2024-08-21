package com.metaro.metaro.tarot.controller;

import com.metaro.metaro.tarot.dto.TarotRequestDTO;
import com.metaro.metaro.tarot.dto.TarotResponse;
import com.metaro.metaro.tarot.service.TarotResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarot")
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
    public ResponseEntity<?> submitTarotReading(@RequestBody TarotRequestDTO requestDTO, @AuthenticationPrincipal Principal principal) {
        // Python 서버로 요청을 전송하기 위해 JSON 데이터 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TarotRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

        // Python 서버로 요청 전송
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(pythonServerUrl + "/chatbot/chat-bot", requestEntity, String.class);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Python 서버와의 통신에 실패했습니다.");
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode()).body("Python 서버로부터의 응답 오류입니다.");
        }

        // AI 서버의 응답을 저장
        tarotResponseService.saveResponse(response.getBody());

        // 클라이언트에 성공 메시지 반환
        return ResponseEntity.ok("타로 리딩 결과가 성공적으로 저장되었습니다.");
    }

    @GetMapping("/result")
    public ResponseEntity<String> getLastTarotReading() {
        Optional<TarotResponse> lastResponse = tarotResponseService.getLastResponse();

        if (lastResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("타로 리딩 결과를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(lastResponse.get().getAnswer());
    }

}