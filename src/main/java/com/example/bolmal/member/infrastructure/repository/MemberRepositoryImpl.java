package com.example.bolmal.member.infrastructure.repository;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import com.example.bolmal.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;


    @Override
    public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username).map(MemberEntity::toModel);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(MemberEntity.fromModel(member)).toModel();
    }

    @Override
    public boolean existsByUsername(String username) {
        return memberJpaRepository.existsByUsername(username);
    }
}
