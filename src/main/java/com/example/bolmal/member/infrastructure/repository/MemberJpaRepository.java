package com.example.bolmal.member.infrastructure.repository;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    List<MemberEntity> findInactiveMembersForDeletion(Status status, LocalDateTime cutoffDate);

    void deleteAll(List<Member> membersToDelete);

}
