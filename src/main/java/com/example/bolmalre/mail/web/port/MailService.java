package com.example.bolmalre.mail.web.port;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public interface MailService {
    void sendEmail(String toEmail) throws MessagingException;

    Boolean verifyEmailCode(String email, String code);

    String makeMemberId(String email) throws NoSuchAlgorithmException;
}
