package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.infrastructure.entity.AgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementJpaRepository extends JpaRepository<AgreementEntity, Long> {
}
