package com.example.bolmalre.alarm.service;

import com.example.bolmalre.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.artist.domain.enums.Genre;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AlarmServiceImplTest {

    @InjectMocks
    AlarmServiceImpl alarmService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AlarmRepository alarmRepository;

    Member testMember;

    @BeforeEach
    void setUp() {

        testMember = Member.builder()
                .id(1L)
                .username("test123")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();
    }



    @Test
    @DisplayName("subscribe() 를 호출하면 구독권이 정해진 만큼 증가한다")
    public void subscribe_success(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));

        //when
        alarmService.subscribe("test123");

        //then
        assertThat(testMember.getAlarmAccount()).isEqualTo(3);
    }


    @Test
    @DisplayName("존재하지 않는 username으로 구독권을 구매하면 정해진 예외를 반환한다")
    public void subscribe_fail(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> alarmService.subscribe("test123"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }
}