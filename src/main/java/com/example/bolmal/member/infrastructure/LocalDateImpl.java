package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.service.port.LocalDate;

import java.time.LocalDateTime;

public class LocalDateImpl implements LocalDate {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
