package com.example.bolmal.artist.infrastructure.entity;

import com.example.bolmal.artist.enums.Genre;
import com.example.bolmal.bookmark.infrastructure.entity.BookmarkEntity;
import com.example.bolmal.common.domain.BaseEntity;
import com.example.bolmal.concert.infrastructure.entity.ConcertEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ArtistEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @OneToMany(mappedBy = "artistEntity", cascade = CascadeType.ALL)
    private List<FavoriteArtistEntity> favoriteArtistEntities = new ArrayList<>();

    @OneToMany(mappedBy = "artistEntity", cascade = CascadeType.ALL)
    private List<BookmarkEntity> bookmarkEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_entity")
    private ConcertEntity concertEntity;
}
