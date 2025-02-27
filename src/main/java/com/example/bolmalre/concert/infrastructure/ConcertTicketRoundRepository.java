package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertTicketRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertTicketRoundRepository extends JpaRepository<ConcertTicketRound, Long> {

    Optional<ConcertTicketRound> findFirstByConcertAndTicketOpenDateAfterOrderByTicketOpenDateAsc(Concert concert, LocalDateTime ticketOpenDate);

    List<ConcertTicketRound> findAllByConcert(Concert concert);
}
