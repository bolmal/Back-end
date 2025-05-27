package com.example.bolmalre.domain.concert.infrastructure;

import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.concert.domain.ConcertPerformanceRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertPerformanceRoundRepository extends JpaRepository<ConcertPerformanceRound,Long> {

    List<ConcertPerformanceRound> findAllByConcert(Concert concert);
}
