package com.example.bolmalre.member.converter;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.web.dto.MemberProfileDTO;

public class MemberConverter {

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
}
