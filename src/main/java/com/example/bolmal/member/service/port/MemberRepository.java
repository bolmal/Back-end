package com.example.bolmal.member.service.port;

import com.example.bolmal.member.infrastructure.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository {

    Optional<MemberEntity> findByUsername(String username);

    MemberEntity save(MemberEntity member);
}
