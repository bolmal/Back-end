package com.example.bolmalre.concert.web.dto;

import com.example.bolmalre.concert.domain.enums.OnlineStore;
import com.example.bolmalre.concert.domain.enums.TicketRound;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class SaveConcertDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Slf4j
    public static class SaveRequestDTO {

        @Schema(description = "공연 명")
        @JsonProperty("concert_name")
        private String concertName;

        @Schema(description = "공연 포스터 이미지")
        @JsonProperty("concert_poster")
        private String posterUrl;

        @Schema(description = "캐스팅 아티스트")
        @JsonProperty("casting")
        private List<ConcertArtistDTO> casting;

        @Schema(description = "공연 일차 및 날짜 정보")
        @JsonProperty("performance_rounds")
        private List<ConcertPerformanceRoundDTO> performanceRounds;

        @Schema(description = "공연 장소")
        @JsonProperty("venue")
        private String concertPlace;

        @Schema(description = "공연 시간")
        @JsonProperty("running_time")
        private Integer concertRuntime;

        @Schema(description = "가격 정보 (좌석에 따라 달라질 수 있음)")
        @JsonProperty("price")
        private Map<String, Integer> price;

        @Schema(description = "공연 제한 나이")
        @JsonProperty("age_limit")
        private String concertAge;

        @Schema(description = "예매 제한 (인당 구매 가능 티켓수)")
        @JsonProperty("booking_limit")
        private String maxTicketsPerPerson;

        @Schema(description = "공연 티켓팅 사이트 종류")
        @JsonProperty("selling_platform")
        private String onlineStore;

        @Schema(description = "티켓 오픈 여부")
        @JsonProperty("ticket_status")
        private Boolean ticketStatus;

        @Schema(description = "티켓팅 오픈 날짜 (선예매 등)")
        @JsonProperty("ticket_open_dates")
        private Map<String, LocalDateTime> ticketOpenDates;

        @Schema(description = "티켓팅할 사이트 링크")
        @JsonProperty("booking_link")
        private String bookingLink;

        @Schema(description = "잡다한 설명들")
        @JsonProperty("additional_info")
        private String additionalInfo;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertPerformanceRoundDTO {

        @Schema(description = "공연 일차 (1일차, 2일차)")
        private Integer round;

        @Schema(description = "공연 일자 (1일차 공연 날짜)")
        private LocalDateTime datetime;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertPriceDTO {

        @Schema(description = "좌석 이름")
        private String seat_type;

        @Schema(description = "좌석 가격")
        private Integer price;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertTicketRoundDTO {

        @Schema(description = "선예매, 일반 예매 구분")
        private String ticket_round;

        @Schema(description = "티켓 오픈 날짜")
        private LocalDateTime ticket_open_date;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ConcertArtistDTO {

        @Schema(description = "아티스트")
        private String name;
    }
}

