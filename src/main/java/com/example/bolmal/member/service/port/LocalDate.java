package com.example.bolmal.member.service.port;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface LocalDate {

    LocalDateTime now();
}
