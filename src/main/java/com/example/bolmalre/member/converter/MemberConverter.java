package com.example.bolmalre.member.converter;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.MemberProfileImage;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.service.port.UuidHolder;
import com.example.bolmalre.member.web.dto.MemberFindPasswordDTO;
import com.example.bolmalre.member.web.dto.MemberFindUsernameDTO;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.example.bolmalre.member.web.dto.MemberProfileDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MemberConverter {

    public static Member toMember(MemberJoinDTO.MemberJoinRequestDTO request, String encodedPassword) {
        return Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .name(request.getName())
                .role(Role.ROLE_USER)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthDate())
                .email(request.getEmail())
                .status(Status.ACTIVE)
                .gender(request.getGender())
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .isLogin(false)
                .build();
    }

    public static MemberProfileDTO.MemberProfileResponseDTO toMemberProfileResponseDTO(Member member) {

        List<MemberProfileImage> memberProfileImages = member.getMemberProfileImages();

        MemberProfileImage first = memberProfileImages.stream().findFirst()
                .orElseThrow(()->new MemberHandler(ErrorStatus.IMAGE_NOT_FOUND));

        return MemberProfileDTO.MemberProfileResponseDTO.builder()
                .username(member.getUsername())
                .name(member.getName())
                .gender(member.getGender())
                .birthDate(member.getBirthday())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .imagePath(first.getImageLink())
                .build();
    }

    public static MemberFindUsernameDTO.MemberFindUsernameResponseDTO memberFindUsernameResponseDTO(Member member) {
        return MemberFindUsernameDTO.MemberFindUsernameResponseDTO.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .build();
    }

    public static MemberFindPasswordDTO.MemberFindPasswordResponseDTO toMemberFindPasswordResponseDTO(Member member, String password) {
        return MemberFindPasswordDTO.MemberFindPasswordResponseDTO.builder()
                .memberId(member.getId())
                .newPassword(password)
                .build();
    }

    public static Member toOAuthMember(String email, String name, String password, BCryptPasswordEncoder passwordEncoder, UuidHolder uuid){

        return Member.builder()
                .username("oauth_" + UUID.randomUUID())
                .password(passwordEncoder.encode(password))
                .name(name)
                .role(Role.ROLE_USER)
                .phoneNumber("oauth_phone_" + uuid.toString())
                .birthday(LocalDate.of(1,1,1))
                .email(email)
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();
    }

    public static Member toFrontKakaoMember(String email, String name, String password, BCryptPasswordEncoder passwordEncoder, UuidHolder uuid){

        return Member.builder()
                .username("front_" + uuid.randomUUID())
                .password(passwordEncoder.encode(password))
                .name(name)
                .role(Role.ROLE_USER)
                .phoneNumber("kakao_phone_" + uuid.randomUUID())
                .birthday(LocalDate.of(1, 1, 1))
                .email(email)
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();
    }

    public static MemberJoinDTO.MemberSocialResponseDTO toMemberSocialResponseDTO(Member member) {
        return MemberJoinDTO.MemberSocialResponseDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .upComming("test")
                .alarmCount(0)
                .bookmarkCount(0)
                .isSubscribe(false)
                .imagePath(null)
                .build();
    }
}
