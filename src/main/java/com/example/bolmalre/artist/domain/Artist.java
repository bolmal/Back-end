package com.example.bolmalre.artist.domain;

import com.example.bolmalre.artist.domain.enums.Genre;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.common.domain.BaseEntity;
import com.example.bolmalre.concert.domain.ConcertArtist;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Artist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    // 추후 아티스트 구현 시 리팩터링 FIXME
    private String profileImagePath;


    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<FavoriteArtist> favoriteArtists = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "artist",cascade = CascadeType.ALL)
    private List<ConcertArtist> concertArtists = new ArrayList<>();



}
