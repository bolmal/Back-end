package com.example.bolmalre.alarm.converter;

import com.example.bolmalre.alarm.domain.Alarm;
import com.example.bolmalre.alarm.web.dto.AlarmReadDTO;

import java.util.List;

public class AlarmConverter {

    public static List<AlarmReadDTO.AlarmReadRequestDTO> toAlarmReadRequestDTO(List<Alarm> byMember){
        return byMember.stream()
                .map(Alarm::getConcert)
                .map(concert -> AlarmReadDTO.AlarmReadRequestDTO.builder()
                        .concertPosterPath(concert.getViewingRestrict())
                        .concertRound(concert.getConcertRound())
                        .ticketOpenDate(concert.getTicketOpenDate())
                        .concertName(concert.getConcertName())
                        .concertDate(concert.getConcertDate())
                        .onlineStore(concert.getOnlineStore())
                        .build())
                .toList();
    }
}
