package com.example.bolmal.member.service;



import com.example.bolmal.member.domain.Agreement;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.MemberRepository;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.port.MemberService;
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
    private final BCrypt bCrypt;


    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request){

        if (!Boolean.TRUE.equals(request.getPrivacyAgreement())
                || !Boolean.TRUE.equals(request.getServiceAgreement())
                || !Boolean.TRUE.equals(request.getFinancialAgreement())) {
            throw new IllegalArgumentException("필수 약관에는 모두 동의를 해주셔야 합니다.");
        }

        Member newMember= Member.JoinDTOto(request,bCrypt);
        Member savedMember = memberRepository.save(newMember);

        // 약관동의 저장
        Agreement newAgreement = Agreement.JoinDTOto(request);


        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .memberId(savedMember.getId())
                .build();

    }
}
