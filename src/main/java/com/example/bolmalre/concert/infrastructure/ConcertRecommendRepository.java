package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.ConcertRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRecommendRepository extends JpaRepository<ConcertRecommend, Long> {
}
