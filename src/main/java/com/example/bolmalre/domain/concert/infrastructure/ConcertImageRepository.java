package com.example.bolmalre.domain.concert.infrastructure;

import com.example.bolmalre.domain.concert.domain.Concert;
import com.example.bolmalre.domain.concert.domain.ConcertImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertImageRepository extends JpaRepository<ConcertImage, Long> {

    Optional<ConcertImage> findByConcert(Concert concert);

    List<ConcertImage> findTop10ByOrderByIdDesc();
}
