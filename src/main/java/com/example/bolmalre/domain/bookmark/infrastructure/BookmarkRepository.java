package com.example.bolmalre.domain.bookmark.infrastructure;


import com.example.bolmalre.domain.artist.domain.Artist;
import com.example.bolmalre.domain.bookmark.domain.Bookmark;
import com.example.bolmalre.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberAndArtist(Member member, Artist artist);
    List<Bookmark> findByMember(Member member);
    List<Bookmark> findByArtistIn(List<Artist> artists);
}
