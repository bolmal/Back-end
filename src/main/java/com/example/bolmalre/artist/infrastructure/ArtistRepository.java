package com.example.bolmalre.artist.infrastructure;

import com.example.bolmalre.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist,Long> {

}
