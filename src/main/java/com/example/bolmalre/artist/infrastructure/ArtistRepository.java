package com.example.bolmalre.artist.infrastructure;

import com.example.bolmalre.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist,Long> {

    List<Artist> findByIdIn(List<Long> ids);

    Optional<Artist> findByName(String name);
}
