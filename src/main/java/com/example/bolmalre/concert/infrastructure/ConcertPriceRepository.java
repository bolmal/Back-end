package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.ConcertPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertPriceRepository extends JpaRepository<ConcertPrice, Long> {
}
