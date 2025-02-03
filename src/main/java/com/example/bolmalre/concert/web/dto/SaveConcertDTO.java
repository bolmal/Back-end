package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.OnlineStore;
import com.example.bolmalre.concert.domain.enums.TicketRound;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SaveConcertDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class SaveRequestDTO {

        @Schema(description = "공연 명")
        private String concertName;

        @Schema(description = "공연 포스터 이미지")
        private String posterUrl;

        @Schema(description = "공연 일차 및 날짜 정보")
        private List<ConcertPerformanceRoundDTO> performanceRounds;

        @Schema(description = "공연 장소")
        private String concertPlace;

        @Schema(description = "공연 시간")
        private String concertRuntime;

        @Schema(description = "가격 정보 (좌석에 따라 달라질 수 있음)")
        private List<ConcertPriceDTO> prices;

        @Schema(description = "공연 제한 나이")
        private Integer concertAge;

        @Schema(description = "예매 제한 (인당 구매 가능 티켓수)")
        private String maxTicketsPerPerson;

        @Schema(description = "공연 티켓팅 사이트 종류")
        private OnlineStore onlineStore;

        @Schema(description = "티켓 오픈 여부")
        private Boolean ticketStatus;

        @Schema(description = "티켓팅 오픈 날짜 (선예매 등)")
        private List<ConcertTicketRoundDTO> ticketOpenDates;

        @Schema(description = "티켓팅할 사이트 링크")
        private String onlineStoreLink;

        @Schema(description = "잡다한 설명들")
        private String description;

    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertPerformanceRoundDTO {

        @Schema(description = "공연 일차 (1일차, 2일차)")
        private Integer Round;

        @Schema(description = "공연 일자 (1일차 공연 날짜)")
        private LocalDateTime concertDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertTicketRoundDTO {

        @Schema(description = "선예매, 일반 예매 ")
        private TicketRound ticketRound;

        @Schema(description = "선예매 오픈 날짜")
        private LocalDateTime ticketOpenDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertPriceDTO {

        @Schema(description = "좌석 이름")
        private String seatType;

        @Schema(description = "좌석 가격")
        private Integer price;

    }
}
