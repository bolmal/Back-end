package com.example.bolmal.bookmark.infrastructure.entity;

import com.example.bolmal.artist.infrastructure.entity.ArtistEntity;
import com.example.bolmal.concert.infrastructure.entity.ConcertEntity;
import com.example.bolmal.member.infrastructure.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_entity")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_entity")
    private ArtistEntity artistEntity;
}
