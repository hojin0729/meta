package com.metaro.metaro.tarot.service;

import com.metaro.metaro.member.domain.Member;
import com.metaro.metaro.tarot.domain.TarotResponse;
import com.metaro.metaro.tarot.repository.TarotResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarotResponseService {

    private final TarotResponseRepository tarotResponseRepository;

    @Autowired
    public TarotResponseService(TarotResponseRepository tarotResponseRepository) {
        this.tarotResponseRepository = tarotResponseRepository;
    }

    public TarotResponse saveResponse(String answer, Member member) {
        TarotResponse tarotResponse = new TarotResponse();
        tarotResponse.setAnswer(answer);
        tarotResponse.setMember(member);
        return tarotResponseRepository.save(tarotResponse);
    }

    public Optional<TarotResponse> getLastResponse() {
        return tarotResponseRepository.findTopByOrderByIdDesc();
    }

    public List<TarotResponse> getAllResponsesByMember(Member member) {
        return tarotResponseRepository.findAllByMemberOrderByIdDesc(member);
    }
}