package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.ConcertRound;
import com.example.bolmalre.concert.domain.enums.OnlineStore;
import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(description = "공연 명")
        String concertName;

        @Schema(description = "공연 회차")
        ConcertRound concertRound;

        @Schema(description = "공연 장소")
        String concertPlace;

        @Schema(description = "공연 일시")
        LocalDate concertDate;

        @Schema(description = "공연 티켓팅 오픈일")
        LocalDateTime ticketOpenDate;

        @Schema(description = "공연 시간")
        String concertRuntime;

        @Schema(description = "공연 가격")
        Integer price;

        @Schema(description = "공연 제한 나이")
        Integer concertAge;

        @Schema(description = "공연 제한 나이")
        String viewingRestrict;

        @Schema(description = "추천도 점수, 바뀔 수 있음")
        Integer recommendRate;

        @Schema(description = "공연 티켓팅 오픈 장소")
        OnlineStore onlineStore;

        Integer viewCount;

        boolean advertisement;

        @Schema(description = "공연 포스터 이미지")
        String posterUrl;

        @Schema(description = "공연 참여 아티스트")
        List<Long> artistId;
    }
}
