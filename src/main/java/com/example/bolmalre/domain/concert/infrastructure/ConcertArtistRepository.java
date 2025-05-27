package com.example.bolmalre.domain.concert.infrastructure;

import com.example.bolmalre.domain.concert.domain.ConcertArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertArtistRepository extends JpaRepository<ConcertArtist, Long> {
}
