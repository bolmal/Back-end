package com.example.bolmal.member.infrastructure.entity;

import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.member.domain.Agreement;
import com.example.bolmal.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AgreementEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean serviceAgreement;

    @Column(nullable = false)
    private Boolean privacyAgreement;

    @Column(nullable = false)
    private Boolean financialAgreement;

    @Column(nullable = false)
    private Boolean advAgreement;



    @OneToOne
    @JoinColumn(name = "member_entity_id")
    private MemberEntity member;


    // Model -> Entity: 모델 정보를 영속성 객체로 바꿀 때
    public static AgreementEntity fromModel(Agreement agreement) {

        AgreementEntity agreementEntity = new AgreementEntity();
        agreementEntity.id = agreement.getId();
        agreementEntity.serviceAgreement = agreement.getServiceAgreement();
        agreementEntity.privacyAgreement = agreement.getPrivacyAgreement();
        agreementEntity.financialAgreement = agreement.getFinancialAgreement();
        agreementEntity.advAgreement = agreement.getAdvAgreement();

        return agreementEntity;
    }


    // Entity -> Model: 엔티티를 모델정보로 전환할 때
    public Agreement toModel() {

        return Agreement.builder()
                .id(id)
                .advAgreement(advAgreement)
                .financialAgreement(financialAgreement)
                .privacyAgreement(privacyAgreement)
                .serviceAgreement(serviceAgreement)
                .build();

    }
}
