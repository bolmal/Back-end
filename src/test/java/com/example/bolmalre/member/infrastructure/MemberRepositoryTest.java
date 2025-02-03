package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {


    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("findInactiveMembersForDeletion() 를 이용하여 비활성화 상태가 30일이 넘은 회원을 조회할 수 있다")
    public void findInactiveMembersForDeletion_success() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 2, 1, 0, 0);
        LocalDateTime cutoffDate = now.minusDays(30);

        Member member1 = Member.builder()
                .username("test1")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test1@example.com")
                .status(Status.INACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .inactiveDate(now.minusDays(31))
                .build();

        Member member2 = Member.builder()
                .username("test2")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test2@example.com")
                .status(Status.INACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .inactiveDate(now.minusDays(40))
                .build();

        Member member3 = Member.builder()
                .username("test3")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test3@example.com")
                .status(Status.INACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .inactiveDate(now.minusDays(25))
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // when
        List<Member> result = memberRepository.findInactiveMembersForDeletion(Status.INACTIVE, cutoffDate);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Member::getUsername)
                .containsExactlyInAnyOrder("test1", "test2");
    }
}
