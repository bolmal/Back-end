package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;


public class ConcertConverter {

    public static ConcertHomeDTO.AdvertisementConcertDTO toAdvertisementConcertDTO(Concert concert, String imageLink) {

        return ConcertHomeDTO.AdvertisementConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(imageLink)
                .build();

    }

    public static ConcertHomeDTO.RecommendConcertDTO toRecommendConcertDTO(Concert concert, String imageLink) {
        return ConcertHomeDTO.RecommendConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(imageLink)
                .concertName(concert.getConcertName())
                .build();
    }

    public static ConcertHomeDTO.WeekHotConcertDTO toWeekHotConcertDTO(Concert concert, String imageLink) {
        return ConcertHomeDTO.WeekHotConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(imageLink)
                .concertName(concert.getConcertName())
                .concertPlace(concert.getConcertPlace())
                .build();
    }

    public static ConcertPageDTO.ConcertInfoDTO toConcertInfoDTO(Concert concert, String imageLink) {
        return ConcertPageDTO.ConcertInfoDTO.builder()
                .id(concert.getId())
                .posterUrl(imageLink)
                .concertName(concert.getConcertName())
                .build();
    }

    public static ConcertDetailPageDTO.ConcertDetailDTO toConcertDetailDTO(Concert concert, String imageLink) {
        return ConcertDetailPageDTO.ConcertDetailDTO.builder().build();
    }

}
