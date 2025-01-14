package com.example.bolmal.member.infrastructure;

import com.example.bolmal.member.service.port.LocalDateTimeHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocalDateTimeHolderImpl implements LocalDateTimeHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime minusDays(long days) {
        return LocalDateTime.now().minusDays(days);
    }
}
