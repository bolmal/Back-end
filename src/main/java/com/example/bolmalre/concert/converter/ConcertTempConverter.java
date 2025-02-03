package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.web.dto.SaveDTO;

import java.util.ArrayList;

public class ConcertTempConverter {

    public static Concert toConcert(SaveDTO.SaveRequestDTO request){

        return Concert.builder()
                .concertName(request.getConcertName())
                .concertRuntime(request.getConcertRuntime())
                .concertPlace(request.getConcertPlace())
                .concertAge(request.getConcertAge())
                .onlineStore(request.getOnlineStore())
                .advertisement(true)
                .posterUrl(request.getPosterUrl())
                .concertArtists(new ArrayList<>())
                .build();
    }
}
