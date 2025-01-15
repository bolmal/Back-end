package com.example.bolmal.member.infrastructure.repository;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT m FROM MemberEntity m WHERE m.status = :status AND m.inactiveDate <= :cutoffDate")
    List<MemberEntity> findInactiveMembersForDeletion(@Param("status") Status status, @Param("cutoffDate") LocalDateTime cutoffDate);

    void deleteAll();

    MemberEntity findByNameAndPhoneNumber(String name, String phoneNumber);

}
