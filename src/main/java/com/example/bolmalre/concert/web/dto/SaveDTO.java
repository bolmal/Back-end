package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.ConcertRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SaveDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class SaveRequestDTO {

        String concertName;

        ConcertRound concertRound;

        String concertPlace;

        LocalDate concertDate;

        LocalDateTime ticketOpenDate;

        String concertRuntime;

        Integer price;

        Integer concertAge;

        String viewingRestrict;

        Integer recommendRate;

        OnlineStore onlineStore;

        Integer viewCount;

        boolean advertisement;

        String posterUrl;

        List<Long> artistId;
    }
}
