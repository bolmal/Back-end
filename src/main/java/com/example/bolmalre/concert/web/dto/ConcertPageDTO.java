package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.TicketRound;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConcertPageDTO {

    // 콘서트 페이지 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertInfoDTO {

        @Schema(description = "공연 id")
        private Long id;

        @Schema(description = "콘서트 포스터 URL")
        private String posterUrl;

        @Schema(description = "1차 티켓오픈, 2차 티켓오픈, 선예매 등 티켓회차")
        private TicketRound ticketRound;

        @Schema(description = "티켓팅 오픈 날짜+시간")
        private LocalDateTime ticketOpenDate;

        @Schema(description = "콘서트 이름")
        private String concertName;

        @Schema(description = "콘서트 공연 일자 (날짜)")
        private LocalDate concertDate;
    }

}
