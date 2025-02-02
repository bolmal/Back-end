package com.example.bolmalre.mail.web.controller;


import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.mail.web.dto.EmailRequestDto;
import com.example.bolmalre.mail.web.port.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
@Tag(name = "이메일 인증 API")
public class MailController {

    private final MailService emailService;

    @GetMapping("/{email_addr}")
    @Operation(summary = "인증 이메일 전송 API")
    public ApiResponse<String> sendEmailPath(@PathVariable String email_addr) throws MessagingException {

        emailService.sendEmail(email_addr);

        return ApiResponse.onSuccess("이메일이 전송되었습니다");
    }

    @PostMapping("/{email_addr}")
    @Operation(summary = "인증 번호 검증 API")
    public ApiResponse<String> sendEmailAndCode(@PathVariable String email_addr,
                                                   @RequestBody EmailRequestDto dto) throws NoSuchAlgorithmException {

        emailService.verifyEmailCode(email_addr, dto.getCode());

        return ApiResponse.onSuccess("이메일 인증에 성공하였습니다");
    }
}
