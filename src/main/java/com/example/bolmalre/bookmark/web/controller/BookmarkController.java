package com.example.bolmalre.bookmark.web.controller;

import com.example.bolmalre.bookmark.web.dto.BookmarkGetArtistDTO;
import com.example.bolmalre.bookmark.web.dto.BookmarkRegisterDTO;
import com.example.bolmalre.bookmark.web.port.BookmarkService;
import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Tag(name = "아티스트 찜 기능 API")
@Builder
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @PostMapping("/subscribes")
    @Operation(summary = "아티스트 찜 구독권 구매 API")
    public ApiResponse<String> subscribeBookmark(@AuthenticationPrincipal UserDetails userDetails) {
        bookmarkService.subscribe(userDetails.getUsername());
        return ApiResponse.onSuccess("성공적으로 구매가 완료되었습니다");
    }


    @PostMapping("")
    @Operation(summary = "아티스트 찜 등록 API")
    public ApiResponse<BookmarkRegisterDTO.BookmarkRegisterResponseDTO> registerBookmark(@AuthenticationPrincipal UserDetails userDetails,
                                                                                         @Valid @RequestBody BookmarkRegisterDTO.BookmarkRegisterRequestDTO request) {

        BookmarkRegisterDTO.BookmarkRegisterResponseDTO result = bookmarkService.register(userDetails.getUsername(), request);

        return ApiResponse.onSuccess(result);
    }


    @GetMapping("")
    @Operation(summary = "찜한 아티스트 조회 API")
    public ApiResponse<List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO>> getArtist(@AuthenticationPrincipal UserDetails userDetails) {

        List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO> result =
                bookmarkService.getArtist(userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }
}
