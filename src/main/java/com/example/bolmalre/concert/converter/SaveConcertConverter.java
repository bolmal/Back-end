package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertPerformanceRound;
import com.example.bolmalre.concert.domain.ConcertPrice;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import com.example.bolmalre.concert.web.dto.SaveConcertDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SaveConcertConverter {

    public static Concert toConcert(SaveConcertDTO.SaveRequestDTO dto) {
        return Concert.builder()
                .concertName(dto.getConcertName())
                .concertPlace(dto.getConcertPlace())
                .posterUrl(dto.getPosterUrl())
                .concertRuntime(dto.getConcertRuntime())
                .concertAge(dto.getConcertAge())
                .maxTicketsPerPerson(dto.getMaxTicketsPerPerson())
                .onlineStore(dto.getOnlineStore())
                .onlineStoreLink(dto.getBookingLink())
                .description(dto.getAdditionalInfo())
                .ticketStatus(dto.getTicketStatus())
                .build();
    }

    public static List<ConcertPrice> toConcertPrices(Map<String, Integer> priceMap, Concert concert) {
        return priceMap.entrySet().stream()
                .map(entry -> ConcertPrice.builder()
                        .seatType(entry.getKey())         // 좌석 종류
                        .price(entry.getValue())          // 좌석 가격
                        .concert(concert)
                        .build()
                ).collect(Collectors.toList());
    }

    public static List<ConcertTicketRound> toConcertTicketRounds(Map<String, String> ticketOpenDatesMap, Concert concert) {
        return ticketOpenDatesMap.entrySet().stream()
                .map(entry -> ConcertTicketRound.builder()
                        .ticketRound(entry.getKey())          // 티켓 구분 (선예매, 일반 예매 등)
                        .ticketOpenDate(LocalDateTime.parse(entry.getValue()))  // 티켓 오픈 날짜
                        .concert(concert)
                        .build()
                ).collect(Collectors.toList());
    }

    public static ConcertPerformanceRound toConcertPerformanceRound(SaveConcertDTO.ConcertPerformanceRoundDTO roundDTO, Concert concert) {
        return ConcertPerformanceRound.builder()
                .round(roundDTO.getRound())
                .concertDate(roundDTO.getDatetime())
                .concert(concert)
                .build();
    }
}
