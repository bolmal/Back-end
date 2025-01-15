package com.example.bolmal.member.service.port;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


public interface LocalDateTimeHolder {

    LocalDateTime now();

    LocalDateTime minusDays(long days);
}
