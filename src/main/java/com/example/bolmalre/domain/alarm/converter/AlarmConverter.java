package com.example.bolmalre.domain.alarm.converter;

import com.example.bolmalre.domain.alarm.domain.Alarm;
import com.example.bolmalre.domain.alarm.web.dto.AlarmReadDTO;

import java.util.List;

public class AlarmConverter {

    public static List<AlarmReadDTO.AlarmReadRequestDTO> toAlarmReadRequestDTO(List<Alarm> byMember){
        return byMember.stream()
                .map(Alarm::getConcert)
                .map(concert -> AlarmReadDTO.AlarmReadRequestDTO.builder()
                        .concertName(concert.getConcertName())
                        .onlineStore(concert.getOnlineStore())
                        .build())
                .toList();
    }
}
