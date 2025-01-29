package com.example.bolmalre.member.domain;


import com.example.bolmalre.common.domain.BaseEntity;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Agreement extends BaseEntity {

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
    @JoinColumn(name = "member_id")
    private Member member;


    public static Agreement JoinDTOto(MemberJoinDTO.MemberJoinRequestDTO request, Member member){
        return Agreement.builder()
                .advAgreement(request.getAdvAgreement())
                .financialAgreement(request.getFinancialAgreement())
                .privacyAgreement(request.getPrivacyAgreement())
                .serviceAgreement(request.getServiceAgreement())
                .member(member)
                .build();
    }
}
