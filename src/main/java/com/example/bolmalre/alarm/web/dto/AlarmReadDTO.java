package com.example.bolmalre.alarm.web.dto;

import com.example.bolmalre.concert.domain.enums.TicketRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlarmReadDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class AlarmReadRequestDTO{

        @Schema(description = "티켓 포스터")
        String concertPosterPath;

        @Schema(description = "티켓 오픈 회차")
        TicketRound ticketRound;

        @Schema(description = "티켓팅 날짜")
        LocalDateTime ticketOpenDate;

        @Schema(description = "공연 이름")
        String concertName;

        @Schema(description = "공연 일시")
        LocalDate concertDate;

        @Schema(description = "티켓팅 주소")
        String onlineStore;
    }
}
