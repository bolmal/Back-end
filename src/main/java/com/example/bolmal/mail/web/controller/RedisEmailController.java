package com.example.bolmal.mail.web.controller;

import com.example.bolmal.mail.service.RedisMailService;
import com.example.bolmal.mail.web.dto.EmailRequestDto;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis_email")
public class RedisEmailController {

    private final RedisMailService emailService;

    @GetMapping("/{email_addr}/authcode")
    public ResponseEntity<String> sendEmailPath(@PathVariable String email_addr) throws MessagingException {
        emailService.sendEmail(email_addr);
        return ResponseEntity.ok("이메일을 확인하세요");
    }

    @PostMapping("/{email_addr}/authcode")
    public ResponseEntity<String> sendEmailAndCode(@PathVariable String email_addr,
                                                   @RequestBody EmailRequestDto dto) throws NoSuchAlgorithmException {
        if (emailService.verifyEmailCode(email_addr, dto.getCode())) {
            return ResponseEntity.ok(emailService.makeMemberId(email_addr));
        }
        return ResponseEntity.notFound().build();
    }
}
