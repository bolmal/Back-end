package com.example.bolmal.auth.mock;

import com.example.bolmal.member.util.CurrentTime;

public class FakeCurrentTime implements CurrentTime {
    @Override
    public long getCurrentTime() {

        // 2025년 1월 1일 00:00의 타임스탬프 (UTC 기준 밀리초)
        return 1735689600000L;
    }
}
