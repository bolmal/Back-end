package com.example.bolmal.mail.web.controller;


import com.example.bolmal.common.apiPayLoad.ApiResponse;
import com.example.bolmal.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mails")
@Tag(name = "이메일 인증 API")
public class MailController {

    private static final Logger log = LoggerFactory.getLogger(MailController.class);
    private final MailService mailService;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    // 인증 이메일 전송
    @Operation(summary = "인증 이메일 전송 API")
    @PostMapping("/mailSend")
    public ApiResponse<String> mailSend(@RequestParam @Email String mail) {
        HashMap<String, Object> map = new HashMap<>();

        log.info("Controller에서 받은 이메일: {}", mail);

        try {
            number = mailService.sendMail(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return ApiResponse.onSuccess("메일이 발송되었습니다");
    }


    // 인증번호 일치여부 확인
    @Operation(summary = "인증번호 일치여부 확인 API")
    @GetMapping("/mailCheck")
    public ApiResponse<String> mailCheck(@RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        if (!isMatch){
            throw new IllegalArgumentException("이메일 인증번호가 일치하지 않습니다");
        }

        // isMatch를 문자열로 변환하여 메시지에 포함
        return ApiResponse.onSuccess("인증이 완료되었습니다");
    }


}