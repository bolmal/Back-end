package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.service.port.BCryptHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptHolderImpl implements BCryptHolder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encode(String password){

        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean matches(String validPassword, String password) {
        return bCryptPasswordEncoder.matches(validPassword, password);
    }


}
