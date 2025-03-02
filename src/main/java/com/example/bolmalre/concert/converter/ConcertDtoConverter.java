package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;

import java.time.LocalDateTime;

public class ConcertDtoConverter {

    // 광고 DTO Converter
    public static ConcertHomeDTO.AdvertisementConcertDTO toAdvertisementConcertDTO(Concert concert) {

        return ConcertHomeDTO.AdvertisementConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .build();

    }

    // 지금 볼래말래 DTO Converter
    public static ConcertHomeDTO.RecommendConcertDTO toRecommendConcertDTO(
            Concert concert,
            String ticketRound,
            LocalDateTime ticketOpenDate,
            String concertDate) {
        return ConcertHomeDTO.RecommendConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .round(ticketRound)
                .ticketOpenDate(ticketOpenDate)
                .concertName(concert.getConcertName())
                .concertDate(concertDate )  // 콘서트 날짜
                .build();
    }

    // 이번주 가장 인기 있는 티켓 DTO Converter
    public static ConcertHomeDTO.WeekHotConcertDTO toWeekHotConcertDTO(
            Concert concert,
            String ticketRound,
            LocalDateTime ticketOpenDate,
            String concertDate
            ) {

        return ConcertHomeDTO.WeekHotConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .round(ticketRound)
                .ticketOpenDate(ticketOpenDate)
                .concertName(concert.getConcertName())
                .concertDate(concertDate)  // 콘서트 날짜
                .concertPlace(concert.getConcertPlace())
                .build();
    }

    // Concert 페이지 DTO
    public static ConcertPageDTO.ConcertInfoDTO toConcertInfoDTO(
            Concert concert,
            ConcertTicketRound ctr,
            LocalDateTime concertPerformanceDate,
            String ticketOpenDate) {
        return ConcertPageDTO.ConcertInfoDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .ticketRound(ctr.getTicketRound())
                .ticketOpenDate(LocalDateTime.parse(ticketOpenDate))
                .concertName(concert.getConcertName())
                .concertDate(String.valueOf(concertPerformanceDate))
                .build();
    }

    public static ConcertDetailPageDTO.ConcertDetailDTO toConcertDetailDTO(Concert concert,
                                                                    String ticketInfo,
                                                                    String concertDate,
                                                                    String ticketPrice
    ) {
        return ConcertDetailPageDTO.ConcertDetailDTO.builder()
                .id(concert.getId())
                .concertName(concert.getConcertName())
                .ticketOpenInfo(ticketInfo)
                .concertPlace(concert.getConcertPlace())
                .concertDate(concertDate)
                .concertRuntime(concert.getConcertRuntime()+"분")
                .price(ticketPrice)
                .onlineStore(concert.getOnlineStore())
                .onlineStoreURL(concert.getOnlineStoreLink())
                .concertAge(concert.getConcertAge())
                .viewingRestrict(concert.getMaxTicketsPerPerson())
                .build();
    }
}
