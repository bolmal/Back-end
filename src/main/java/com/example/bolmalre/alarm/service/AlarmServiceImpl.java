package com.example.bolmalre.alarm.service;

import com.example.bolmalre.alarm.converter.AlarmConverter;
import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.alarm.infrastructure.AlarmRepository;
import com.example.bolmalre.alarm.web.dto.AlarmReadDTO;
import com.example.bolmalre.alarm.web.port.AlarmService;
import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.AlarmHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ConcertHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.infrastructure.ConcertRepository;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final ConcertRepository concertRepository;

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String configEmail;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void subscribe(String username){
        Member findMember = findMemberByUsername(username);

        Member.alarmAccountPlus(findMember);
    }


    @Override
    public void register(String username, Long concertId){

        Member findMember = findMemberByUsername(username);
        Concert findConcert = findConcertById(concertId);

        Alarm newAlarm = Alarm.builder()
                .member(findMember)
                .concert(findConcert)
                .build();

        validAlarmAccount(findMember);
        Member.alarmAccountMinus(findMember);
        validAlarmExist(findMember, findConcert);

        alarmRepository.save(newAlarm);
    }


    @Override
    public List<AlarmReadDTO.AlarmReadRequestDTO> get(String username){

        Member memberByUsername = findMemberByUsername(username);
        List<Alarm> byMember = alarmRepository.findByMember(memberByUsername);

        return AlarmConverter.toAlarmReadRequestDTO(byMember);
    }


    @Override
    public void alarmMail(String email) throws MessagingException {

        if (!isValidEmail(email)) {
            throw new MailHandler(ErrorStatus.MAIL_NOT_VALID);
        }

        MimeMessage emailForm = createEmailForm(email);
        mailSender.send(emailForm);

    }


    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private String setContext() {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("alarm", context);
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[볼래말래] 공연 정보 업데이트 알림");
        message.setFrom(configEmail);
        message.setText(setContext(), "utf-8", "html");

        return message;
    }








    private static void validAlarmAccount(Member findMember) {
        if (findMember.getAlarmAccount().equals(0)){
            throw new AlarmHandler(ErrorStatus.ALARM_ACCOUNT_ZERO);
        }
    }

    private void validAlarmExist(Member findMember, Concert findConcert) {
        boolean valid = alarmRepository.existsByMemberAndConcert(findMember, findConcert);
        if (valid){
            throw new AlarmHandler(ErrorStatus.ALARM_EXISTS);
        }
    }

    private Concert findConcertById(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(()-> new ConcertHandler(ErrorStatus.CONCERT_NOT_FOUND));
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
