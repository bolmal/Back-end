package com.example.bolmal.member.mock;

import com.example.bolmal.member.domain.Agreement;
import com.example.bolmal.member.service.port.AgreementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FakeAgreementRepository implements AgreementRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final List<Agreement> data = new ArrayList<>();


    @Override
    public Agreement save(Agreement agreement) {
        Agreement agreement1 = Agreement.builder()
                .id(counter.incrementAndGet())
                .advAgreement(agreement.getAdvAgreement())
                .privacyAgreement(agreement.getPrivacyAgreement())
                .serviceAgreement(agreement.getServiceAgreement())
                .financialAgreement(agreement.getFinancialAgreement())
                .build();

        data.add(agreement1);

        return agreement1;
    }

}
