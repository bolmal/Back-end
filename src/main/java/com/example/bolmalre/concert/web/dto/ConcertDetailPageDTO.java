package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.TicketRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConcertDetailPageDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertDetailDTO {

        private Long id;

        @Schema(description = "공연 이름")
        private String concertName;

        @Schema(description = "티켓팅 회차 및 오픈 시간 (1차 예매, 2차 예매)")
        private String ticketOpenInfo;

        @Schema(description = "공연 장소")
        private String concertPlace;

        @Schema(description = "공연 날짜")
        private String concertDate;

        @Schema(description = "공연 시간")
        private String concertRuntime;

        @Schema(description = "콘서트 가격 (좌석 정보 함께 제공)")
        private String price;

        @Schema(description = "온라인 스토어 종류")
        private String onlineStore;

        @Schema(description = "온라인 스토어 링크")
        private String onlineStoreURL;

        @Schema(description = "연령 제한")
        private String concertAge;

        @Schema(description = "예매 제한")
        private String viewingRestrict;


    }
}
