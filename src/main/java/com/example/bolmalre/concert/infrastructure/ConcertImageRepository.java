package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertImageRepository extends JpaRepository<ConcertImage, Long> {

    Optional<ConcertImage> findByConcert(Concert concert);
}
