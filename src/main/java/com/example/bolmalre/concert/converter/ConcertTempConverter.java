package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.web.dto.SaveDTO;

import java.util.ArrayList;

public class ConcertTempConverter {

    public static Concert toConcert(SaveDTO.SaveRequestDTO request){

        return Concert.builder()
                .concertName(request.getConcertName())
                .concertRound(request.getConcertRound())
                .concertDate(request.getConcertDate())
                .ticketOpenDate(request.getTicketOpenDate())
                .concertRuntime(request.getConcertRuntime())
                .concertPlace(request.getConcertPlace())
                .price(request.getPrice())
                .concertAge(request.getConcertAge())
                .viewingRestrict(request.getViewingRestrict())
                .onlineStore(request.getOnlineStore())
                .viewCount(request.getViewCount())
                .recommendRate(request.getRecommendRate())
                .advertisement(true)
                .posterUrl(request.getPosterUrl())
                .concertArtists(new ArrayList<>())
                .build();
    }
}
