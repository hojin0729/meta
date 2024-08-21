package com.metaro.metaro.tarot.repository;

import com.metaro.metaro.member.domain.Member;
import com.metaro.metaro.tarot.domain.TarotResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarotResponseRepository extends JpaRepository<TarotResponse, Long> {
    Optional<TarotResponse> findTopByOrderByIdDesc();

    List<TarotResponse> findAllByMemberOrderByIdDesc(Member member);
}