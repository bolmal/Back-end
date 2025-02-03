package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.TicketRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
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
        private String concertName;
        private TicketRound ticketRound;
        private String concertPlace;
        private LocalDate concertDate;
        private LocalDateTime ticketOpenDate;
        private String concertRuntime;
        private Integer price;
        private Integer concertAge;
        private String viewingRestrict;
        private Integer recommendRate;
        private OnlineStore onlineStore;
    }
}
