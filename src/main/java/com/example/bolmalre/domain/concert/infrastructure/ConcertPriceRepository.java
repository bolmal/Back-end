package com.example.bolmalre.domain.concert.infrastructure;

import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.concert.domain.ConcertPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertPriceRepository extends JpaRepository<ConcertPrice, Long> {

    List<ConcertPrice> findAllByConcert(Concert concert);
}
