package com.example.bolmalre.alarm.util;

import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.alarm.web.port.AlarmService;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.service.port.LocalDateHolder;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.MAIL_NOT_SEND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AlarmMailUtilTest {

    @InjectMocks
    private AlarmMailUtil alarmMailUtil;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private AlarmRepository alarmRepository;

    @Mock
    private AlarmService alarmService;

    @Mock
    private LocalDateHolder localDateHolder;

    Concert testConcert1;
    Concert testConcert2;
    ConcertTicketRound ticketRound1;
    ConcertTicketRound ticketRound2;
    Member testMember1;
    Member testMember2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.of(2024, 2, 1, 0, 0);
        when(localDateHolder.now()).thenReturn(now);

        testConcert1 = Concert.builder()
                .concertName("test1")
                .build();

        testConcert2 = Concert.builder()
                .concertName("test2")
                .build();

        ticketRound1 = ConcertTicketRound.builder()
                .concert(testConcert1)
                .ticketOpenDate(now.plusDays(3))
                .build();

        ticketRound2 = ConcertTicketRound.builder()
                .concert(testConcert2)
                .ticketOpenDate(now.plusDays(7))
                .build();

        testMember1 = Member.builder()
                .email("user1@example.com")
                .build();

        testMember2 = Member.builder()
                .email("user2@example.com")
                .build();
    }

    @Test
    @DisplayName("sendAlarmMail() 을 통해 alarmService를 호출할 수 있다")
    void sendAlarmMail_success() throws MessagingException {
        // given
        LocalDateTime now = localDateHolder.now();
        LocalDateTime oneWeekLater = now.plusDays(7);

        Alarm testAlarm1 = Alarm.builder()
                .member(testMember1)
                .concert(testConcert1)
                .build();

        Alarm testAlarm2 = Alarm.builder()
                .member(testMember2)
                .concert(testConcert2)
                .build();

        when(concertRepository.findConcertsWithTicketsOpeningInOneWeek(now, oneWeekLater))
                .thenReturn(List.of(testConcert1, testConcert2));
        when(alarmRepository.findByConcert(testConcert1))
                .thenReturn(List.of(testAlarm1));
        when(alarmRepository.findByConcert(testConcert2))
                .thenReturn(List.of(testAlarm2));

        // when
        alarmMailUtil.sendAlarmMail();

        // then
        verify(alarmService, times(1)).alarmMail("user1@example.com");
        verify(alarmService, times(1)).alarmMail("user2@example.com");
    }

    @Test
    @DisplayName("메일 발송에 실패하면 정해진 예외를 반환한다")
    void sendAlarmMail_fail() throws MessagingException {
        // given
        doThrow(new MessagingException()).when(alarmService).alarmMail(anyString());
        when(concertRepository.findConcertsWithTicketsOpeningInOneWeek(any(), any()))
                .thenReturn(List.of(testConcert1));
        when(alarmRepository.findByConcert(testConcert1))
                .thenReturn(List.of(Alarm.builder().member(testMember1).concert(testConcert1).build()));

        // when & then
        assertThatThrownBy(() -> alarmMailUtil.sendAlarmMail())
                .isInstanceOf(MailHandler.class)
                .hasFieldOrPropertyWithValue("code", MAIL_NOT_SEND);

        verify(alarmService, atLeastOnce()).alarmMail(anyString());
    }
}