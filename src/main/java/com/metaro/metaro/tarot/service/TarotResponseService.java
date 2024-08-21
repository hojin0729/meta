package com.metaro.metaro.tarot.service;

import com.metaro.metaro.tarot.dto.TarotResponse;
import com.metaro.metaro.tarot.repository.TarotResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TarotResponseService {

    private final TarotResponseRepository tarotResponseRepository;

    @Autowired
    public TarotResponseService(TarotResponseRepository tarotResponseRepository) {
        this.tarotResponseRepository = tarotResponseRepository;
    }

    public TarotResponse saveResponse(String answer) {
        TarotResponse tarotResponse = new TarotResponse();
        tarotResponse.setAnswer(answer);
        return tarotResponseRepository.save(tarotResponse);
    }

    public Optional<TarotResponse> getLastResponse() {
        return tarotResponseRepository.findTopByOrderByIdDesc();
    }
}