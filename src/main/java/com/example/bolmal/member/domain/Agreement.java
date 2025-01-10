package com.example.bolmal.member.domain;


import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Agreement {

    Long id;

    Boolean serviceAgreement;

    Boolean privacyAgreement;

    Boolean financialAgreement;

    Boolean advAgreement;

}
