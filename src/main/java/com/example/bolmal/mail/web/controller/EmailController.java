package com.example.bolmal.mail.web.controller;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
import com.example.bolmal.mail.service.MailServiceImpl;
import com.example.bolmal.mail.web.dto.EmailRequestDto;
import com.example.bolmal.mail.web.port.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@Tag(name = "이메일 인증 API")
public class EmailController {

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

        if (emailService.verifyEmailCode(email_addr, dto.getCode())) {
            return ApiResponse.onSuccess(emailService.makeMemberId(email_addr));
        }

        return ApiResponse.onFailure("FAIL","인증에 실패하였습니다",null);
    }
}
