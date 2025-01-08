package com.example.bolmal.member.infrastructure;

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
    public Optional<MemberEntity> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username);
    }

    @Override
    public MemberEntity save(MemberEntity member) {
        return memberJpaRepository.save(member);
    }
}
