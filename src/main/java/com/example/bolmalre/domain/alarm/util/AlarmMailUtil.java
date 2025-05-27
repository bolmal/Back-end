package com.example.bolmalre.domain.alarm.util;

import com.example.bolmalre.domain.alarm.domain.Alarm;
import com.example.bolmalre.domain.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.domain.alarm.web.port.AlarmService;
import com.example.bolmalre.global.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.global.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.domain.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.domain.member.domain.Member;
import com.example.bolmalre.domain.member.service.port.LocalDateHolder;
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

        List<String> emails = concertRepository.findConcertsWithTicketsOpeningInOneWeek(now, oneWeekLater).stream()
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
