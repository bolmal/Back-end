package com.example.bolmalre.member.service;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.converter.MemberConverter;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

        Member result = authenticateNameAndPhoneNumber(request.getName(), request.getPhoneNumber());

        return MemberConverter.memberFindUsernameResponseDTO(result);
    }

    @Override
    public MemberFindPasswordDTO.MemberFindPasswordResponseDTO getPassword(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request) {

        authenticateUsername(request);

        String newPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        Member result = authenticateNameAndPhoneNumber(request.getName(), request.getPhoneNumber());
        Member.resetPassword(result, newPassword);

        return MemberConverter.toMemberFindPasswordResponseDTO(result,request.getNewPassword());
    }

    @Override
    public MemberJoinDTO.MemberSocialResponseDTO social(MemberJoinDTO.MemberSocialRequestDTO requestDTO) {

        Member byEmail = memberRepository.findByEmail(requestDTO.getEmail())
                .orElse(null);

        if (byEmail == null) {
            byEmail = Member.builder()
                    .username("front_" + UUID.randomUUID())
                    .password(bCryptPasswordEncoder.encode("front"))
                    .name(requestDTO.getName())
                    .role(Role.ROLE_USER)
                    .phoneNumber("kakao_phone_" + UUID.randomUUID())
                    .birthday(LocalDate.of(1, 1, 1))
                    .email(requestDTO.getEmail())
                    .status(Status.ACTIVE)
                    .gender(Gender.MALE)
                    .alarmAccount(0)
                    .bookmarkAccount(0)
                    .subStatus(SubStatus.UNSUBSCRIBE)
                    .build();
            memberRepository.save(byEmail);
        }

        return MemberJoinDTO.MemberSocialResponseDTO.builder()
                .memberId(byEmail.getId())
                .name(byEmail.getName())
                .upComming("test")
                .alarmCount(0)
                .bookmarkCount(0)
                .isSubscribe(false)
                .imagePath(null)
                .build();
    }






    private Member authenticateNameAndPhoneNumber(String name, String phoneNumber) {
        Member result = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);
        if(result == null){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return result;
    }

    private void authenticateUsername(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request) {
        Member memberByUsername = findMemberByUsername(request.getUsername());
        if (memberByUsername == null){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
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
