package com.example.bolmal.member.web.controller;

import com.example.bolmal.common.apiPayLoad.ApiResponse;
import com.example.bolmal.member.service.MemberPhoneNumberAuthenticateServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phone-numbers")
@Tag(name = "전화번호 인증 API")
public class PhoneNumberAuthenticateController {

    private MemberPhoneNumberAuthenticateServiceImpl memberPhoneNumberAuthenticateService;

    @Operation(summary = "인증번호 발급")
    @GetMapping("/passwords")
    public ApiResponse<String> getPassword(@RequestParam String phoneNumber){
        String password = memberPhoneNumberAuthenticateService.getPassword(phoneNumber);

        return ApiResponse.onSuccess(password);
    }

    @Operation(summary = "인증번호 인증")
    @GetMapping("/authenticate")
    public ApiResponse<Boolean> authenticate(@RequestParam String password){
        boolean authenticateResult = memberPhoneNumberAuthenticateService.authenticate(password);
        return ApiResponse.onSuccess(authenticateResult);
    }

}
