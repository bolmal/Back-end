package com.example.bolmalre.mail.service;

import com.example.bolmalre.common.apiPayLoad.exception.handler.ImageHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MailHandler;
import com.example.bolmalre.common.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.NoSuchAlgorithmException;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mailService, "configEmail", "test@example.com");
    }


    @Nested
    @DisplayName("sendEmail() 을 이용하여 이메일을 발송 할 수 있다")
    class SendEmailTest {

        @Test
        @DisplayName("이메일 발송 성공")
        void sendEmail_Success() throws MessagingException {
            // given
            String toEmail = "user@example.com";
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(redisUtil.existData(toEmail)).thenReturn(false);

            // when
            mailService.sendEmail(toEmail);

            // then
            verify(mailSender, times(1)).send(any(MimeMessage.class));
            verify(redisUtil, times(1)).setDataExpire(
                    argThat(key -> key.startsWith("Email_Auth: ")),
                    anyString(),
                    eq(60 * 5L)
            );
        }


        @Test
        @DisplayName("이메일 발송 시 기존 인증 코드가 존재한다면 삭제한다")
        void sendEmail_delete() throws MessagingException {
            // given
            String toEmail = "user@example.com";
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            when(redisUtil.existData(toEmail)).thenReturn(true);

            // when
            mailService.sendEmail(toEmail);

            // then
            verify(redisUtil, times(1)).deleteData(toEmail);
        }
    }


    @Nested
    @DisplayName("verifyEmailCode()를 이용하여 인증번호를 인증 할 수 있다")
    class VerifyEmailCodeTest {

        @Test
        @DisplayName("올바른 인증 코드 검증 성공")
        void verifyEmailCode_Success() {
            // given
            String email = "user@example.com";
            String code = "123456";
            String redisKey = "Email_Auth: " + email;

            when(redisUtil.getData(redisKey)).thenReturn(code);

            // when
            Boolean result = mailService.verifyEmailCode(email, code);

            // then
            assertTrue(result);
        }


        @Test
        @DisplayName("존재하지 않는 인증코드를 입력하면 정해진 예외를 반환한다")
        void verifyEmailCode_NotFound() {
            // given
            String email = "user@example.com";
            String code = "123456";
            String redisKey = "Email_Auth: " + email;

            when(redisUtil.getData(redisKey)).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> mailService.verifyEmailCode(email, code))
                    .isInstanceOf(MailHandler.class)
                    .hasFieldOrPropertyWithValue("code", MAIL_NOT_SEND);
        }


        @Test
        @DisplayName("잘못된 인증코드를 입력하면 정해진 예외를 반환한다")
        void verifyEmailCode_Invalid() {
            // given
            String email = "user@example.com";
            String code = "123456";
            String error_code = "654321";
            String redisKey = "Email_Auth: " + email;

            when(redisUtil.getData(redisKey)).thenReturn(code);

            // when & then
            assertThatThrownBy(() -> mailService.verifyEmailCode(email, error_code))
                    .isInstanceOf(MailHandler.class)
                    .hasFieldOrPropertyWithValue("code", MAIL_NOT_VALID);
        }
    }


    @Nested
    @DisplayName("회원 ID 생성 테스트")
    class MakeMemberIdTest {

        @Test
        @DisplayName("동일한 이메일로 다른 시간에 생성된 ID는 다르다")
        void generateDifferentIdsForSameEmail() throws NoSuchAlgorithmException, InterruptedException {
            // given
            String email = "user@example.com";

            // when
            String firstId = mailService.makeMemberId(email);
            Thread.sleep(1000); // 1초 대기
            String secondId = mailService.makeMemberId(email);

            // then
            assertNotEquals(firstId, secondId);
        }


        @Test
        @DisplayName("다른 이메일로 생성된 ID는 다르다")
        void generateDifferentIdsForDifferentEmails() throws NoSuchAlgorithmException {
            // given
            String email1 = "user1@example.com";
            String email2 = "user2@example.com";

            // when
            String id1 = mailService.makeMemberId(email1);
            String id2 = mailService.makeMemberId(email2);

            // then
            assertNotEquals(id1, id2);
        }
    }


    @Test
    @DisplayName("인증코드는 정해진 형식에 따라 생성된다")
    void validateGeneratedCodeFormat() throws MessagingException {
        // given
        String toEmail = "test@example.com";
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        mailService.sendEmail(toEmail);
        verify(redisUtil).setDataExpire(anyString(), codeCaptor.capture(), anyLong());
        String capturedCode = codeCaptor.getValue();

        // then
        assertNotNull(capturedCode);
        assertEquals(6, capturedCode.length());
        assertTrue(capturedCode.matches("[0-9A-Za-z]{6}"));
    }
}