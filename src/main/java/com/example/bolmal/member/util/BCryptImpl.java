package com.example.bolmal.member.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptImpl {

    public static String encode(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
