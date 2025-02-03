package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE c.ticketOpenDate BETWEEN :startDate AND :endDate")
    List<Concert> findConcertsWithTicketOpeningInAWeek(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
