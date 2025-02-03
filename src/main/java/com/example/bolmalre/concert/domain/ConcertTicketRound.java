package com.example.bolmalre.concert.domain;

import com.example.bolmalre.common.domain.BaseEntity;
import com.example.bolmalre.concert.domain.enums.TicketRound;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ConcertTicketRound extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TicketRound ticketRound;

    private LocalDateTime ticketOpenDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;
}
