package com.example.bolmalre.bookmark.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkRegisterDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class BookmarkRegisterRequestDTO{

        Long artistId;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class BookmarkRegisterResponseDTO{

       Long id;

    }
}
