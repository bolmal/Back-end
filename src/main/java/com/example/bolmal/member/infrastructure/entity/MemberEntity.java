package com.example.bolmal.member.infrastructure.entity;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String email;

    // 회원 ACTIVE,INACTIVE 상태 추가
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    // 회원 비활성화 상태 날짜를 기록하는 필드
    private LocalDateTime inactiveDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;







    // Model -> Entity: 모델 정보를 영속성 객체로 바꿀 때
    public static MemberEntity fromModel(Member member) {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.id = member.getId();
        memberEntity.username = member.getUsername();
        memberEntity.password = member.getPassword();
        memberEntity.name = member.getName();
        memberEntity.nickname = member.getNickname();
        memberEntity.role = member.getRole();
        memberEntity.phoneNumber = member.getPhoneNumber();
        memberEntity.birthday = member.getBirthday();
        memberEntity.email = member.getEmail();
        memberEntity.status = member.getStatus();
        memberEntity.inactiveDate = member.getInactiveDate();
        memberEntity.gender = member.getGender();

        return memberEntity;
    }


    // Entity -> Model: 엔티티를 모델정보로 전환할 때
    public static Member toModel(MemberEntity memberEntity) {

        return Member.builder()
                .id(memberEntity.getId())
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .name(memberEntity.getName())
                .nickname(memberEntity.getNickname())
                .role(memberEntity.getRole())
                .phoneNumber(memberEntity.getPhoneNumber())
                .birthday(memberEntity.getBirthday())
                .email(memberEntity.getEmail())
                .status(memberEntity.getStatus())
                .inactiveDate(memberEntity.getInactiveDate())
                .gender(memberEntity.getGender())
                .build();

    }
}
