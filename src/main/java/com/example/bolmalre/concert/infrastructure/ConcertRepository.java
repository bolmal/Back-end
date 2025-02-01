package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
