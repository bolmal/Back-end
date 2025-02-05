package com.example.bolmalre.alarm.service;

import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.alarm.web.dto.AlarmReadDTO;
import com.example.bolmalre.common.apiPayLoad.exception.handler.AlarmHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ConcertHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.service.port.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AlarmServiceImplTest {

    @InjectMocks
    AlarmServiceImpl alarmService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AlarmRepository alarmRepository;

    @Mock
    ConcertRepository concertRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    Member testMember;

    Concert testConcert;

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

        testConcert = Concert.builder()
                .concertName("test1")
                .concertPlace("서울 공연장")
                .posterUrl("https://example.com/poster.jpg")
                .concertRuntime("test") // 120분
                .concertAge("12세 이상")
                .maxTicketsPerPerson("test")
                .onlineStore("예매 사이트")
                .onlineStoreLink("https://example.com/booking")
                .description("테스트 콘서트 설명")
                .ticketStatus(true)
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


    @Test
    @DisplayName("register()를 이용하여 알림을 등록 할 수 있다")
    public void register_success(){
        //given
        Member.alarmAccountPlus(testMember);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(concertRepository.findById(1L)).thenReturn(Optional.of(testConcert));

        //when
        alarmService.register("test123",1L);

        //then
        assertThat(testMember.getAlarmAccount()).isEqualTo(2);
        verify(alarmRepository,times(1)).save(any(Alarm.class));
    }


    @Test
    @DisplayName("존재하지 않는 username으로 등록을 시도하면 정해진 예외를 반환한다")
    public void register_MemberNotFound(){
        //given
        String errorUsername = "ERROR";
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> alarmService.register(errorUsername,1L))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("존재하지 않는 콘서트로 등록을 시도하면 정해진 예외를 반환한다")
    public void register_ConcertNotFound(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(concertRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> alarmService.register("test123",11233L))
                .isInstanceOf(ConcertHandler.class)
                .hasFieldOrPropertyWithValue("code", CONCERT_NOT_FOUND);
    }


    @Test
    @DisplayName("이미 알람이 등록되어 있는 공연에 등록을 시도하면 정해진 예외를 반환한다")
    public void register_Exist(){
        //given
        Member.alarmAccountPlus(testMember);

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(concertRepository.findById(any())).thenReturn(Optional.of(testConcert));
        when(alarmRepository.existsByMemberAndConcert(any(),any())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> alarmService.register("test123",1L))
                .isInstanceOf(AlarmHandler.class)
                .hasFieldOrPropertyWithValue("code", ALARM_EXISTS);
    }


    @Test
    @DisplayName("알림 가능 횟수가 0일때 등록을 시도하면 정해진 예외를 반환한다")
    public void register_AccountZero(){
        //given
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(concertRepository.findById(any())).thenReturn(Optional.of(testConcert));

        // when & then
        assertThatThrownBy(() -> alarmService.register("test123",1L))
                .isInstanceOf(AlarmHandler.class)
                .hasFieldOrPropertyWithValue("code", ALARM_ACCOUNT_ZERO);
    }


    @Test
    @DisplayName("get()을 이용하여 회원이 알림 신청한 공연의 정보를 조회 할 수 있다")
    public void get_success(){
        //given
        Alarm testAlarm = Alarm.builder()
                .member(testMember)
                .concert(testConcert)
                .build();

        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(testMember));
        when(alarmRepository.findByMember(any())).thenReturn(List.of(testAlarm));

        //when
        List<AlarmReadDTO.AlarmReadRequestDTO> response = alarmService.get("test123");

        //then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getConcertName()).isEqualTo("test1");
    }


    @Test
    @DisplayName("존재하지 않는 회원이 조회를 요청하면 정해진 예외를 반환한다")
    public void get_MemberNotFound(){
        //given
        String errorUsername = "ERROR";

        // when & then
        assertThatThrownBy(() -> alarmService.get(errorUsername))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("alarm()을 이용하여 알림을 보낼 수 있다")
    public void alarm_success() throws MessagingException {
        //given
        String toEmail = "user@example.com";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        //when
        alarmService.alarmMail(toEmail);

        //then
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }


    @Test
    @DisplayName("이메일 형식에 맞지 않으면 정해진 예외를 반환한다")
    public void alarm_pattern(){
        //given
        String toEmail = "error";

        //when & then
        assertThatThrownBy(() -> alarmService.alarmMail(toEmail))
                .isInstanceOf(MailHandler.class)
                .hasFieldOrPropertyWithValue("code", MAIL_NOT_VALID);
    }


    @Test
    @DisplayName("이메일이 입력되지 않으면 정해진 예외를 반환한다")
    public void alarm_email_null(){
        //given
        String toEmail = "";

        //when & then
        assertThatThrownBy(() -> alarmService.alarmMail(toEmail))
                .isInstanceOf(MailHandler.class)
                .hasFieldOrPropertyWithValue("code", MAIL_NOT_VALID);
    }
}