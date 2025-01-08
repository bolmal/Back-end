package com.example.bolmal.config;

import com.example.bolmal.member.util.BCrypt;
import com.example.bolmal.member.util.BCryptImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BCryptConfig {

    @Bean
    public BCrypt bcrypt() {
        return new BCryptImpl();
    }
}
