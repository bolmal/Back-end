package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT m FROM Member m WHERE m.status = :status AND m.inactiveDate <= :cutoffDate")
    List<Member> findInactiveMembersForDeletion(Status status, LocalDateTime cutoffDate);

    Member findByNameAndPhoneNumber(String name, String phoneNumber);
}
