package com.example.bolmal.mail.web.controller;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
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
    public ApiResponse<String> sendEmailPath(@PathVariable String email_addr) throws MessagingException {

        emailService.sendEmail(email_addr);

        return ApiResponse.onSuccess("이메일이 전송되었습니다");
    }

    @PostMapping("/{email_addr}/authcode")
    public ApiResponse<String> sendEmailAndCode(@PathVariable String email_addr,
                                                   @RequestBody EmailRequestDto dto) throws NoSuchAlgorithmException {

        if (emailService.verifyEmailCode(email_addr, dto.getCode())) {
            return ApiResponse.onSuccess(emailService.makeMemberId(email_addr));
        }

        return ApiResponse.onFailure("FAIL","인증에 실패하였습니다",null);
    }
}
