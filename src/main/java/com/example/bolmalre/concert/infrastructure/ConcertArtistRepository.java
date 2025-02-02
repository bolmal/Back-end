package com.example.bolmalre.concert.infrastructure;

import com.example.bolmalre.concert.domain.Concert;
import com.example.bolmalre.concert.domain.ConcertArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertArtistRepository extends JpaRepository<ConcertArtist, Long> {
}
