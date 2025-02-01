package com.example.bolmalre.bookmark.converter;

import com.example.bolmalre.artist.domain.Artist;
import com.example.bolmalre.bookmark.domain.Bookmark;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import com.example.bolmalre.member.domain.Member;

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
}
