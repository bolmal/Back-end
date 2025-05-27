package com.example.bolmalre.domain.concert.infrastructure;

import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.concert.domain.ConcertTicketRound;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertTicketRoundRepository extends JpaRepository<ConcertTicketRound, Long> {

    Optional<ConcertTicketRound> findFirstByConcertAndTicketOpenDateAfterOrderByTicketOpenDateAsc(Concert concert, LocalDateTime ticketOpenDate);

    List<ConcertTicketRound> findAllByConcert(Concert concert);

    @Query("""
    SELECT tr FROM ConcertTicketRound tr
    WHERE tr.ticketOpenDate >= :now
    ORDER BY tr.ticketOpenDate ASC
    LIMIT 1
""")
    Optional<ConcertTicketRound> findNearestConcertTicket(@Param("now") LocalDateTime now);
}
