package com.example.bolmal.member.service;



import com.example.bolmal.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmal.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmal.member.domain.Agreement;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.service.port.AgreementRepository;
import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.LocalDateTimeHolder;
import com.example.bolmal.member.service.port.MemberRepository;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberProfileDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import com.example.bolmal.member.web.port.MemberService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AgreementRepository agreementRepository;
    private final BCrypt bCrypt;
    private final LocalDateTimeHolder localDate;


    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request){

        authenticateAgreement(request);
        Member newMember= Member.JoinDTOto(request,bCrypt);
        Member savedMember = memberRepository.save(newMember);

        Agreement newAgreement = Agreement.JoinDTOto(request,savedMember);
        agreementRepository.save(newAgreement);

        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .memberId(savedMember.getId())
                .build();
    }

    @Override
    public MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request,
                                                          String username) {

        Member findMember = findMemberByUsername(username);
        Member updatedMember = Member.update(request, findMember);

        memberRepository.save(updatedMember);

        return MemberUpdateDTO.MemberUpdateResponseDTO.builder()
                .memberId(updatedMember.getId())
                .build();
    }

    @Override
    public MemberProfileDTO.MemberProfileResponseDTO get(String username) {

        Member findMember = findMemberByUsername(username);

        return Member.toMemberProfileResponseDTO(findMember);
    }

    @Override
    public void delete(String username){

        Member findMember = findMemberByUsername(username);
        Member deletedMember = Member.delete(findMember, localDate);

        memberRepository.save(deletedMember);
    }

    @Override
    public void rollback(String username){

        Member findMember = findMemberByUsername(username);
        Status status = findMember.getStatus();

        if (status.equals(Status.ACTIVE)) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_INACTIVE);
        }

        Member rollbackMember = Member.rollback(findMember, localDate);

        memberRepository.save(rollbackMember);
    }

    @Override
    public String resetPassword(String username, String newPassword){

        Member findMember = findMemberByUsername(username);

        Member changedMember = Member.resetPassword(findMember, newPassword, bCrypt);
        memberRepository.save(changedMember);

        return changedMember.getUsername();

    }





    @Scheduled(cron = "0 0 0 * * ?")
    public void final_delete(){
        deleteOldInactiveMembers(30);
    }

    @Override
    public void deleteOldInactiveMembers(long days) {

        LocalDateTime cutoffDate = localDate.minusDays(days);
        List<Member> membersToDelete = memberRepository.findInactiveMembersForDeletion(Status.INACTIVE, cutoffDate);

        // 30일 지난 회원 삭제
        memberRepository.deleteAll(membersToDelete);

    }

    private static void authenticateAgreement(MemberJoinDTO.MemberJoinRequestDTO request) {
        if (!Boolean.TRUE.equals(request.getPrivacyAgreement())
                || !Boolean.TRUE.equals(request.getServiceAgreement())
                || !Boolean.TRUE.equals(request.getFinancialAgreement())) {
            throw new MemberHandler(ErrorStatus.MEMBER_AGREEMENT);
        }
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
