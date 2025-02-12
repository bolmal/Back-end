package com.example.bolmalre.concert.converter;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertHomeDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;
import com.example.bolmalre.concert.web.dto.SaveConcertDTO;

import java.util.List;


public class ConcertConverter {

    // 광고 DTO Converter
    public static ConcertHomeDTO.AdvertisementConcertDTO toAdvertisementConcertDTO(Concert concert) {

        return ConcertHomeDTO.AdvertisementConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .build();

    }

    // 지금 볼래말래 DTO Converter
    public static ConcertHomeDTO.RecommendConcertDTO toRecommendConcertDTO(
            Concert concert, List<SaveConcertDTO.ConcertTicketRoundDTO> concertTicketRoundDTOList,
            ConcertHomeDTO.DateRangeDTO concertRange) {
        return ConcertHomeDTO.RecommendConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .concertTicketRoundDTOList(concertTicketRoundDTOList)
                .concertName(concert.getConcertName())
                .concertDate(concertRange)  // 콘서트 날짜
                .build();
    }

    // 이번주 가장 인기 있는 티켓 DTO Converter
    public static ConcertHomeDTO.WeekHotConcertDTO toWeekHotConcertDTO(
            Concert concert, List<SaveConcertDTO.ConcertTicketRoundDTO> concertTicketRoundDTOList,
            ConcertHomeDTO.DateRangeDTO concertRange) {
        return ConcertHomeDTO.WeekHotConcertDTO.builder()
                .id(concert.getId())
                .posterUrl(concert.getPosterUrl())
                .concertTicketRoundDTOList(concertTicketRoundDTOList)
                .concertName(concert.getConcertName())
                .concertDate(concertRange)  // 콘서트 날짜
                .concertPlace(concert.getConcertPlace())
                .build();
    }

    // Concert 페이지 DTO
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
