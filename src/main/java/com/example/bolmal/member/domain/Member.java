package com.example.bolmal.member.domain;

import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.MemberRepository;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
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


}
