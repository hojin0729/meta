package com.metaro.metaro.refreshToken.domain;

import com.metaro.metaro.member.domain.Authority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private String id;
    private String ip;
    @Enumerated(EnumType.STRING)
    private Authority authorities;
    private String refreshToken;

    @Builder
    public RefreshToken(String id, String ip, Authority authorities, String refreshToken) {
        this.id = id;
        this.ip = ip;
        this.authorities = authorities;
        this.refreshToken = refreshToken;
    }
}