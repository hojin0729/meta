package com.metaro.metaro.refreshToken.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String id;
    private String ip;
    private Collection<? extends GrantedAuthority> authorities;
    private String refreshToken;

    @Builder
    public RefreshToken(String id, String ip, Collection<? extends GrantedAuthority> authorities, String refreshToken) {
        this.id = id;
        this.ip = ip;
        this.authorities = authorities;
        this.refreshToken = refreshToken;
    }
}