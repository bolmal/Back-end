package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertPriceRepository extends JpaRepository<ConcertPrice, Long> {

    List<ConcertPrice> findAllByConcert(Concert concert);
}
