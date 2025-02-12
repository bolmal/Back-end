package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.service.port.LocalDateHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LocalDateHolderImpl implements LocalDateHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
