package com.example.bolmal.member.domain;


import com.example.bolmal.member.web.dto.MemberJoinDTO;
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


    public static Agreement JoinDTOto(MemberJoinDTO.MemberJoinRequestDTO request){
        return Agreement.builder()
                .advAgreement(request.getAdvAgreement())
                .financialAgreement(request.getFinancialAgreement())
                .privacyAgreement(request.getPrivacyAgreement())
                .serviceAgreement(request.getServiceAgreement())
                .build();
    }

}
