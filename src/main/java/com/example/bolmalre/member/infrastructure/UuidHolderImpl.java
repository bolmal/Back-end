package com.example.bolmalre.member.infrastructure;

import com.example.bolmalre.member.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UuidHolderImpl implements UuidHolder {

    @Override
    public String randomUUID(){
        return UUID.randomUUID().toString();
    }
}
