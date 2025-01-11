package com.example.bolmal.concert.infrastructure.entity;

import com.example.bolmal.concert.domain.enums.ConcertRound;
import com.example.bolmal.concert.domain.enums.OnlineStore;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String concertName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConcertRound concertRound;

    @Column(nullable = false)
    private String concertPlace;

    @Column(nullable = false)
    private LocalDate concertDate;

    @Column(nullable = false)
    private String concertRuntime;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer concertAge;

    @Column(nullable = false)
    private String restricts;

    @Column(nullable = false)
    private Integer recommendRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OnlineStore onlineStore;

}
