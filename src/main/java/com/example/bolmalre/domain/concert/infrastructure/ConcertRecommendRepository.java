package com.example.bolmalre.domain.concert.infrastructure;

import com.example.bolmalre.domain.concert.domain.ConcertRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRecommendRepository extends JpaRepository<ConcertRecommend, Long> {
}
