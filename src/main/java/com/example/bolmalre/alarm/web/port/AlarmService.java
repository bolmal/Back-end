package com.example.bolmalre.alarm.web.port;

import com.example.bolmalre.alarm.web.dto.AlarmReadDTO;
import jakarta.mail.MessagingException;

import java.util.List;

public interface AlarmService {
    void subscribe(String username);

    void register(String username, Long concertId);

    List<AlarmReadDTO.AlarmReadRequestDTO> get(String username);

    void alarmMail(String email) throws MessagingException;
}
