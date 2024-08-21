package com.metaro.metaro.tarot.domain;

import com.metaro.metaro.member.domain.Member;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TarotResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 로그인한 회원과의 관계를 나타냅니다.
}