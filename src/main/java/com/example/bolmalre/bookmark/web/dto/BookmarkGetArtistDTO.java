package com.example.bolmalre.bookmark.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkGetArtistDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class BookmarkGetArtistResponseDTO{

        @Schema(description = "아티스트 프로필 사진입니다")
        String artistProfileImage;

        @Schema(description = "아티스트 명입니다")
        String artistName;

        @Schema(description = "아티스트 장르입니다")
        String genre;
    }
}
