package com.example.bolmalre.member.service;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.web.dto.*;
import com.example.bolmalre.member.web.port.MemberService;
import com.example.bolmalre.member.domain.Agreement;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.AgreementRepository;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AgreementRepository agreementRepository;

    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(@Valid MemberJoinDTO.MemberJoinRequestDTO request){

        authenticateAgreement(request);
        authenticateUsernameValid(request.getUsername());

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        Member newMember= Member.JoinDTOto(request,encodedPassword);
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

        return MemberProfileDTO.MemberProfileResponseDTO.builder()
                .username(member.getUsername())
                .name(member.getName())
                .gender(member.getGender())
                .birthDate(member.getBirthday())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .imagePath(member.getProfileImage())
                .build();
    }

    @Override
    public void delete(String username) {

    }

    @Override
    public void rollback(String username) {

    }

    @Override
    public String resetPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request) {
        return "";
    }

    @Override
    public void validPassword(String username, MemberUpdateDTO.MemberPasswordUpdateRequestDTO request) {

    }

    @Override
    public MemberFindUsernameDTO.MemberFindUsernameResponseDTO getUsername(MemberFindUsernameDTO.MemberFindUsernameRequestDTO request) {
        return null;
    }

    @Override
    public MemberFindPasswordDTO.MemberFindPasswordResponseDTO getPassword(MemberFindPasswordDTO.MemberFindPasswordRequestDTO request) {
        return null;
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
