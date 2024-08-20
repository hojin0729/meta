package com.metaro.metaro.member.repository;

import com.metaro.metaro.member.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
}
