package com.example.bolmalre.member.service;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.converter.MemberConverter;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.infrastructure.LocalDateHolder;
import com.example.bolmalre.member.web.dto.*;
import com.example.bolmalre.member.web.port.MemberService;
import com.example.bolmalre.member.domain.Agreement;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.AgreementRepository;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AgreementRepository agreementRepository;

    private final LocalDateHolder localDateHolder;

    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request){

        authenticateAgreement(request);
        authenticateUsernameValid(request.getUsername());

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        Member newMember= MemberConverter.toMember(request,encodedPassword);
        Member savedMember = memberRepository.save(newMember);

        Agreement newAgreement = Agreement.JoinDTOto(request,savedMember);
        agreementRepository.save(newAgreement);

        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .memberId(savedMember.getId())
                .build();
    }

    @Override
    @Transactional
    public MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request, String username) {

        Member memberByUsername = findMemberByUsername(username);
        authenticateUsernameUpdateValid(memberByUsername, request.getUsername());

        Member.update(memberByUsername,request);

        memberRepository.save(memberByUsername);

        return MemberUpdateDTO.MemberUpdateResponseDTO.builder()
                .memberId(memberByUsername.getId())
                .build();
    }

    @Override
    public MemberProfileDTO.MemberProfileResponseDTO get(String username) {

        Member member = findMemberByUsername(username);

        return MemberConverter.toMemberProfileResponseDTO(member);
    }

    @Override
    public void delete(String username) {

        Member memberByUsername = findMemberByUsername(username);

        if (memberByUsername.getStatus().equals(Status.INACTIVE)){
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_INACTIVE);
        }

        Member.delete(memberByUsername,localDateHolder);
    }

    @Override
    public void rollback(String username) {

        Member memberByUsername = findMemberByUsername(username);

        if (memberByUsername.getStatus().equals(Status.ACTIVE)){
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_ACTIVE);
        }

        Member.rollback(memberByUsername,localDateHolder);
    }

    @Override
    public String resetPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request) {

        Member memberByUsername = findMemberByUsername(username);
        String newPassword = bCryptPasswordEncoder.encode(request.getNewPassword());

        Member.resetPassword(memberByUsername,newPassword);

        return request.getNewPassword();
    }

    @Override
    public void validPassword(String username, MemberPasswordValidDTO.MemberPasswordValidRequestDTO request) {

        Member memberByUsername = findMemberByUsername(username);

        if(!bCryptPasswordEncoder.matches(request.getValidPassword(), memberByUsername.getPassword())){
            throw new MemberHandler(ErrorStatus.MEMBER_PASSWORD_VALID);
        }
    }

    @Override
    public MemberFindUsernameDTO.MemberFindUsernameResponseDTO getUsername(MemberFindUsernameDTO.MemberFindUsernameRequestDTO request) {

        Member result = memberRepository.findByNameAndPhoneNumber(request.getName(), request.getPhoneNumber());

        if(result == null){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return MemberConverter.memberFindUsernameResponseDTO(result);
    }

    @Override
    public MemberFindPasswordDTO.MemberFindPasswordResponseDTO getPassword(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request) {
        return null;
    }




    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void deleteOldInactiveMembers() {

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);  // 30일 이전 날짜 계산
        List<Member> membersToDelete = memberRepository.findInactiveMembersForDeletion(Status.INACTIVE, cutoffDate);

        // 30일 지난 회원 삭제
        agreementRepository.deleteAllByMemberIn(membersToDelete);
    }

    private void authenticateUsernameValid(String username) {
        boolean usernameValid = memberRepository.existsByUsername(username);

        if (usernameValid) {
            throw new MemberHandler(ErrorStatus.MEMBER_USERNAME_DUPLICATE);
        }
    }

    private void authenticateUsernameUpdateValid(Member member, String newUsername) {

        if (member.getUsername().equals(newUsername)) {
            return;
        }

        boolean usernameExists = memberRepository.existsByUsername(newUsername);
        if (usernameExists) {
            throw new MemberHandler(ErrorStatus.MEMBER_USERNAME_DUPLICATE);
        }
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
