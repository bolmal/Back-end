package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertTicketRoundRepository extends JpaRepository<ConcertTicketRound, Long> {

    Optional<ConcertTicketRound> findByConcert(Concert concert);
}
