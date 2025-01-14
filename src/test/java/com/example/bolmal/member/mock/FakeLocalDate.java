package com.example.bolmal.member.mock;

import com.example.bolmal.member.service.port.LocalDate;

import java.time.LocalDateTime;

public class FakeLocalDate implements LocalDate {

    LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

    @Override
    public LocalDateTime now() {
        return localDateTime;
    }
}
