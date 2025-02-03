package com.example.bolmalre.alarm.util;

import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.alarm.web.port.AlarmService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.LocalDateHolder;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlarmMailUtil {

    private final AlarmService alarmService;
    private final ConcertRepository concertRepository;
    private final AlarmRepository alarmRepository;

    private final LocalDateHolder localDateHolder;

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendAlarmMail() {
        LocalDateTime now = localDateHolder.now();
        LocalDateTime oneWeekLater = now.plusDays(7);

        List<String> emails = concertRepository.findConcertsWithTicketOpeningInAWeek(now, oneWeekLater).stream()
                .flatMap(concert -> alarmRepository.findByConcert(concert).stream()) // Concert -> Alarm 리스트 변환
                .map(Alarm::getMember) // Alarm -> Member 변환
                .map(Member::getEmail) // Member -> Email 변환
                .distinct() // 중복 이메일 제거
                .toList();

        emails.forEach(email -> {
            try {
                alarmService.alarmMail(email);
            } catch (MessagingException e) {
                throw new MailHandler(ErrorStatus.MAIL_NOT_SEND);
            }
        });
    }

}
