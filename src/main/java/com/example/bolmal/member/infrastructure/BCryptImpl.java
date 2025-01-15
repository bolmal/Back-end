package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.service.port.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptImpl implements BCrypt {

    @Override
    public String encode(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword){

        /**
         첫번째 파라미터: 원본 비밀번호
         두번째 파라미터: 인코딩된 비밀번호
         * */
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
