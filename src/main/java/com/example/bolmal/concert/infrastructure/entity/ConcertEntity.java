package com.example.bolmal.concert.infrastructure.entity;

import com.example.bolmal.concert.domain.enums.ConcertRound;
import com.example.bolmal.concert.domain.enums.OnlineStore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String concertName;

    private ConcertRound concertRound;

    private String concertPlace;

    private LocalDate concertDate;

    private String concertRuntime;

    private Integer price;

    private Integer concertAge;

    private String restricts;

    private Integer recommendRate;

    private OnlineStore onlineStore;

}
