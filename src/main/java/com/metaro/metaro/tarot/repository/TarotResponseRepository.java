package com.metaro.metaro.tarot.repository;

import com.metaro.metaro.tarot.dto.TarotResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarotResponseRepository extends JpaRepository<TarotResponse, Long> {
    Optional<TarotResponse> findTopByOrderByIdDesc();
}