package com.example.bolmalre.member.domain;

import com.example.bolmalre.common.domain.BaseEntity;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.infrastructure.LocalDateHolder;
import com.example.bolmalre.member.web.dto.MemberUpdateDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@DynamicUpdate
public class Member extends BaseEntity {

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
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String email;

    // 회원 ACTIVE, INACTIVE 상태 추가
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

    @Setter
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<MemberProfileImage> memberProfileImages;





    public static void update(Member member, MemberUpdateDTO.MemberUpdateRequestDTO request) {
        member.username = request.getUsername();
        member.name = request.getName();
        member.gender = request.getGender();
        member.phoneNumber = request.getPhoneNumber();
        member.email = request.getEmail();
        member.birthday = request.getBirthDate();
    }

    public static void delete(Member member, LocalDateHolder localDateHolder) {
        member.inactiveDate = localDateHolder.now();
        member.status = Status.INACTIVE;
    }

    public static void rollback(Member member, LocalDateHolder localDateHolder) {
        member.inactiveDate = localDateHolder.now();
        member.status = Status.ACTIVE;
    }

    public static void resetPassword(Member member, String newPassword){
        member.password = newPassword;
    }

    public void removeMemberProfileImage(MemberProfileImage memberProfileImage) {
        memberProfileImages.remove(memberProfileImage);
    }
}
