package com.metaro.metaro.refreshToken.repository;

import com.metaro.metaro.refreshToken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
}
