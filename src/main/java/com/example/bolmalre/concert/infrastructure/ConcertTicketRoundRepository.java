package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.ConcertTicketRound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertTicketRoundRepository extends JpaRepository<ConcertTicketRound, Long> {
}
