package com.example.bolmal.member.infrastructure.entity;

import com.example.bolmal.alarm.infrastructure.entity.AlarmEntity;
import com.example.bolmal.artist.infrastructure.entity.FavoriteArtistEntity;
import com.example.bolmal.bookmark.infrastructure.entity.BookmarkEntity;
import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.domain.enums.SubStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@DynamicUpdate
public class MemberEntity extends BaseEntity {

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
    private Status status;

    // 회원 비활성화 상태 날짜를 기록하는 필드
    private LocalDateTime inactiveDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImage;

    @Column(nullable = false)
    private Integer alarmAccount;

    @Column(nullable = false)
    private Integer bookmarkAccount;

    @Column(nullable = false)
    private SubStatus subStatus;









    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AgreementEntity agreementEntity;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<FavoriteArtistEntity> favoriteArtistEntities = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<AlarmEntity> alarmEntities = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<BookmarkEntity> bookmarkEntities = new ArrayList<>();







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
        memberEntity.profileImage = member.getProfileImage();

        return memberEntity;
    }


    // Entity -> Model: 엔티티를 모델정보로 전환할 때
    public Member toModel() {

        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .name(name)
                .nickname(nickname)
                .role(role)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .email(email)
                .status(status)
                .inactiveDate(inactiveDate)
                .gender(gender)
                .build();

    }
}
