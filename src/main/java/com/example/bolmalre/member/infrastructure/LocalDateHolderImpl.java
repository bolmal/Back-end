package com.example.bolmalre.member.infrastructure;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocalDateHolderImpl implements LocalDateHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
