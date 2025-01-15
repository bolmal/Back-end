package com.example.bolmal.mail.service;

import com.example.bolmal.mail.util.RedisUtil;
import com.example.bolmal.mail.web.port.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import org.thymeleaf.context.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Profile("local")
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Value("${management.mail.username}")
    private String configEmail;

    /**
     *
     * 제작한 이메일을 전송합니다
     *
     * 이때 redis에 이미 해당 이메일로 인증번호가 전송된 이력이 있다면,
     * 삭제하고 대체합니다
     *
     * */
    @Override
    public void sendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        MimeMessage emailForm = createEmailForm(toEmail);

        mailSender.send(emailForm);
    }

    /**
     *
     * 이메일을 검증합니다
     * email을 키값으로 갖는 데이터를 조회하여 일치하는지 조사합니다
     *
     * */
    @Override
    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }




    /**
     *
     * 타임리프와 연동하여 이메일 폼을 제작합니다
     *
     * */
    private String setContext(String code) {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", code);


        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }


    /**
     *
     * 타임리프와 연동하여 메일폼을 만드는 setContext()를
     * 이용하여 이메일 전체를 제작합니다
     *
     * createCode() 메서드를 이용해서 인증번호를 생성합니다
     *
     * 그 후에 redis의 setData를 이용해서 데이터를 redis에 저장하고 시간을 설정합니다
     * 현재는 5분으로 설정해놓은 상태입니다
     *
     * 5분이 지나면 value가 null이 됩니다
     *
     * */
    private MimeMessage createEmailForm(String email) throws MessagingException {

        String authCode = createdCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[볼래말래] 회원가입 이메일 2차확인 인증번호");
        message.setFrom(configEmail);
        message.setText(setContext(authCode), "utf-8", "html");

        redisUtil.setDataExpire(email, authCode, 60 * 5L);

        return message;
    }

    /**
     *
     * 해당하는 멤버ID를 제작하는 메서드입니다
     *
     * 추후 멤버식별자를 구성할 때 사용 할 수 있겠지만,
     * 당장은 auto_increasement로 사용하겠습니다
     *
     * */
    @Override
    public String makeMemberId(String email) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(email.getBytes());
        md.update(LocalDateTime.now().toString().getBytes());
        return Arrays.toString(md.digest());
    }

    /**
     *
     * 랜덤코드를 발급하는 메서드입니다
     *
     * */
    private String createdCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <=57 || i >=65) && (i <= 90 || i>= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

