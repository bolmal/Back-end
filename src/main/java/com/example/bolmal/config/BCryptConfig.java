package com.example.bolmal.config;

import com.example.bolmal.member.service.port.BCrypt;
import com.example.bolmal.member.service.port.MemberRepository;
import com.example.bolmal.member.infrastructure.BCryptImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BCryptConfig {

    @Bean
    public BCrypt bcrypt() {
        return new BCryptImpl();
    }
}
