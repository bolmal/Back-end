package com.example.bolmalre.member.converter;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.web.dto.MemberFindUsernameDTO;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import com.example.bolmalre.member.web.dto.MemberProfileDTO;

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
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();
    }

    public static MemberProfileDTO.MemberProfileResponseDTO toMemberProfileResponseDTO(Member member) {
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

    public static MemberFindUsernameDTO.MemberFindUsernameResponseDTO memberFindUsernameResponseDTO(Member member) {
        return MemberFindUsernameDTO.MemberFindUsernameResponseDTO.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .build();
    }
}
