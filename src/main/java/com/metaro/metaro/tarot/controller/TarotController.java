package com.metaro.metaro.tarot.controller;

import com.metaro.metaro.tarot.dto.TarotRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/tarot")
@RequiredArgsConstructor
public class TarotController {

    private final RestTemplate restTemplate = new RestTemplate();;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    @PostMapping("/submit")
    public ResponseEntity<?> submitTarotReading(@RequestBody TarotRequestDTO requestDTO) {
        // Python 서버로 요청을 전송하기 위해 JSON 데이터 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TarotRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

        // Python 서버로 요청 전송
        ResponseEntity<String> response = restTemplate.postForEntity(pythonServerUrl + "/process-tarot", requestEntity, String.class);

        // Python 서버의 응답을 그대로 반환
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}