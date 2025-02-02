package com.example.bolmalre.bookmark.converter;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.web.dto.BookmarkGetArtistDTO;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import com.example.bolmalre.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

public class BookmarkConverter {

    public static Bookmark toBookmark(Member member, Artist artist){
        return Bookmark.builder()
                .member(member)
                .artist(artist)
                .build();
    }

    public static BookmarkRegisterDTO.BookmarkRegisterResponseDTO toBookmarkRegisterResponseDTO(Bookmark bookmark){

        return BookmarkRegisterDTO.BookmarkRegisterResponseDTO.builder()
                .id(bookmark.getId())
                .build();
    }

    public static List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO> toBookmarkGetArtistResponseDTO(List<Bookmark> bookmarks){
        return bookmarks.stream()
                .map(Bookmark::getArtist)
                .map(artist -> BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO.builder()
                        .artistProfileImage(artist.getProfileImagePath())
                        .artistName(artist.getName())
                        .genre(artist.getGenre().name())
                        .build())
                .collect(Collectors.toList());
    }
}
