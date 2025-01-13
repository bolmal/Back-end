package com.example.bolmal.member.infrastructure.repository;

import com.example.bolmal.member.domain.Agreement;
import com.example.bolmal.member.infrastructure.entity.AgreementEntity;
import com.example.bolmal.member.service.port.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AgreementRepositoryImpl implements AgreementRepository {

    private final AgreementJpaRepository agreementJpaRepository;

    @Override
    public Agreement save(Agreement agreement) {
        return agreementJpaRepository.save(AgreementEntity.fromModel(agreement)).toModel();
    }
}
