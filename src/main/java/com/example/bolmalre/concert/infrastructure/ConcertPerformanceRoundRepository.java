package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertPerformanceRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertPerformanceRoundRepository extends JpaRepository<ConcertPerformanceRound,Long> {

    List<ConcertPerformanceRound> findAllByConcert(Concert concert);
}
