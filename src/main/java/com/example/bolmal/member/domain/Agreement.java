package com.example.bolmal.member.domain;


import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class Agreement extends BaseEntity {

    Long id;

    Boolean serviceAgreement;

    Boolean privacyAgreement;

    Boolean financialAgreement;

    Boolean advAgreement;

    Member member;


    public static Agreement JoinDTOto(MemberJoinDTO.MemberJoinRequestDTO request,Member member){
        return Agreement.builder()
                .advAgreement(request.getAdvAgreement())
                .financialAgreement(request.getFinancialAgreement())
                .privacyAgreement(request.getPrivacyAgreement())
                .serviceAgreement(request.getServiceAgreement())
                .member(member)
                .build();
    }

    private static void setMember(Agreement agreement,Member member){
        agreement.setMember(member);
    }

}
