package com.example.bolmalre.alarm.web.port;

public interface AlarmService {
    void subscribe(String username);

    void register(String username, Long concertId);
}
