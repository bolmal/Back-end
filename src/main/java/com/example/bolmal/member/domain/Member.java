package com.example.bolmal.member.domain;

import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.LocalDateTimeHolder;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberProfileDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class Member extends BaseEntity {


    private Long id;

    private String username;

    private String password;

    private String name;

    private String nickname;

    private Role role;

    private String phoneNumber;

    private LocalDate birthday;

    private String email;

    private Status status;

    private LocalDateTime inactiveDate;

    private Gender gender;

    private Agreement agreement;

    private String profileImage;


    public static Member JoinDTOto(MemberJoinDTO.MemberJoinRequestDTO request, BCrypt bCrypt){

        return Member.builder()
                .username(request.getUsername())
                .password(bCrypt.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.ROLE_USER)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthDate())
                .email(request.getEmail())
                .status(Status.ACTIVE)
                .gender(request.getGender())
                .build();
    }

    public static Member update(MemberUpdateDTO.MemberUpdateRequestDTO request, Member member){


        member.setUsername(request.getUsername());
        // 비밀번는 따로 변경하기

        member.setName(request.getName());
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setBirthday(request.getBirthDate());
        member.setEmail(request.getEmail());
        member.setPhoneNumber(request.getPhoneNumber());

        return member;

    }

    public static MemberProfileDTO.MemberProfileResponseDTO toMemberProfileResponseDTO(Member member){

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

    public static Member delete(Member member, LocalDateTimeHolder localDate){

        member.setStatus(Status.INACTIVE);
        member.setInactiveDate(localDate.now());

        return member;
    }

    public static Member rollback(Member member, LocalDateTimeHolder localDate){

        member.setStatus(Status.ACTIVE);
        member.setInactiveDate(localDate.now());

        return member;
    }


}
