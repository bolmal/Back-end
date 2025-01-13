package com.example.bolmal.member.service;



import com.example.bolmal.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmal.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmal.member.domain.Agreement;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.service.port.AgreementRepository;
import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.MemberRepository;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import com.example.bolmal.member.web.port.MemberService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AgreementRepository agreementRepository;
    private final BCrypt bCrypt;


    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request){

        if (!Boolean.TRUE.equals(request.getPrivacyAgreement())
                || !Boolean.TRUE.equals(request.getServiceAgreement())
                || !Boolean.TRUE.equals(request.getFinancialAgreement())) {
            throw new MemberHandler(ErrorStatus.MEMBER_AGREEMENT);
        }

        Member newMember= Member.JoinDTOto(request,bCrypt);
        Member savedMember = memberRepository.save(newMember);

        // 약관동의 저장
        Agreement newAgreement = Agreement.JoinDTOto(request);
        agreementRepository.save(newAgreement);

        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .memberId(savedMember.getId())
                .build();
    }

    @Override
    public MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request) {
        throw new IllegalArgumentException("TDD-RED");
    }
}
