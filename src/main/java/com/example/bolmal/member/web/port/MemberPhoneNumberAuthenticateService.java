package com.example.bolmal.member.web.port;

import org.springframework.stereotype.Service;

@Service
public interface MemberPhoneNumberAuthenticateService {
    
    boolean authenticate(String password);
    
    String getPassword(String phoneNumber);
}
