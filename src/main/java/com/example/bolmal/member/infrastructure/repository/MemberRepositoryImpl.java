package com.example.bolmal.member.infrastructure.repository;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import com.example.bolmal.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Member> findInactiveMembersForDeletion(Status status, LocalDateTime cutoffDate) {
        return memberJpaRepository.findInactiveMembersForDeletion(status, cutoffDate)
                .stream()
                .map(MemberEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(List<Member> membersToDelete) {
        // Member -> MemberEntity로 변환
        List<MemberEntity> memberEntities = membersToDelete.stream()
                .map(MemberEntity::fromModel)  // 변환
                .collect(Collectors.toList());

        // 변환된 MemberEntity 리스트를 deleteAll 메서드에 전달
        memberJpaRepository.deleteAll(memberEntities);
    }


}
