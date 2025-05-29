package com.example.bolmalre.domain.concert.web.dto;

import com.example.bolmalre.domain.concert.domain.Concert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConcertRankedResponseDTO {

    @Schema(description = "추천하는 공연 id")
    private Long id;

    @Schema(description = "콘서트 포스터 URL")
    private String posterUrl;

    @Schema(description = "선예매, 1차 예매, 2차 예매")
    private String round;

    @Schema(description = "티켓 오픈 날짜")
    private LocalDateTime ticketOpenDate;

    @Schema(description = "콘서트 이름")
    private String concertName;

    @Schema(description = "콘서트 공연 일자 (날짜)")
    private String concertDate;

    @Schema(description = "콘서트 공연 장소")
    private String place;


    public static ConcertRankedResponseDTO of(Concert request){

        return ConcertRankedResponseDTO.builder()
                .id(request.getId())
                .posterUrl(request.getPosterUrl())
                .round(request.getConcertTicketRounds().get(0).getTicketRound())
                .ticketOpenDate(request.getConcertTicketRounds().get(0).getTicketOpenDate())
                .concertName(request.getConcertName())
                .concertDate(request.getConcertPerformanceRounds().get(0).getConcertDate().toString())
                .place(request.getConcertPlace())
                .build();
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertRankedResponseDTOList{

        List<ConcertRankedResponseDTO> concertRankedResponseDTOList;


        public static ConcertRankedResponseDTOList of(List<ConcertRankedResponseDTO> request){
            return ConcertRankedResponseDTOList.builder()
                    .concertRankedResponseDTOList(request)
                    .build();
        }
    }
}
