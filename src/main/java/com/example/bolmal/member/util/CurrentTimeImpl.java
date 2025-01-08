package com.example.bolmal.member.util;

import org.springframework.stereotype.Component;

@Component
public class CurrentTimeImpl implements CurrentTime {

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
