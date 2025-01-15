package com.example.bolmal.member.service.port;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByUsername(String username);

    Member save(Member member);

    boolean existsByUsername(String username);

    List<Member> findInactiveMembersForDeletion(Status status, LocalDateTime cutoffDate);

    void deleteAll(List<Member> membersToDelete);

    Member findByNameAndPhoneNumber(String name, String phoneNumber);
}
