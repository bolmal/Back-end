package com.example.bolmal.member.infrastructure;

import com.example.bolmal.auth.service.port.CurrentTime;
import org.springframework.stereotype.Component;

@Component
public class CurrentTimeImpl implements CurrentTime {

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
