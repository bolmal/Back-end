package com.example.bolmal.member.domain;

import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Member {


    private Long id;

    private String username;

    private String password;

    private String name;

    private String nickname;

    private Role role;

    private String phoneNumber;

    private LocalDate birthday;

    private String email;

    private Status status = Status.ACTIVE;

    private LocalDateTime inactiveDate;

    private Gender gender;



}
