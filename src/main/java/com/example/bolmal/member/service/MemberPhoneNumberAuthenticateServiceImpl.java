package com.example.bolmal.member.service;

import com.example.bolmal.member.web.port.MemberPhoneNumberAuthenticateService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class MemberPhoneNumberAuthenticateServiceImpl implements MemberPhoneNumberAuthenticateService {

    private final String password = "1234";

    @Override
    public boolean authenticate(String newPassword) {
        return newPassword.equals(password);
    }

    @Override
    public String getPassword(String phoneNumber) {
        return "1234";
    }

}
