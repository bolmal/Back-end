package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.domain.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
