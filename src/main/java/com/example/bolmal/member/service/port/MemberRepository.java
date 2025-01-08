package com.example.bolmal.member.service.port;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByUsername(String username);

    Member save(Member member);
}
