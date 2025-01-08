package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.service.port.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptImpl implements BCrypt {

    @Override
    public String encode(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
