package com.example.bolmal.member.infrastructure.repository;

import com.example.bolmal.member.infrastructure.entity.AgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementJpaRepository extends JpaRepository<AgreementEntity, Long> {
}
