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

    /**
     찜 등록하기

     아티스트를 찜하는 기능입니다
     찜하면 회원의 찜 쿠폰이 하나 줄어들고

     아티스트의 공연이 업로드 될 때마다 그 사람한테 메일을 보내줍니다

     찜 설정이 완료되면 찜 설정확인 메일을 전송합니다
     알림받을 아티스트 정보
     * */
    @PostMapping("")
    @Operation(summary = "아티스트 찜 등록 API")
    public ApiResponse<BookmarkRegisterDTO.BookmarkRegisterResponseDTO> registerBookmark(@AuthenticationPrincipal UserDetails userDetails,
                                                                                         @Valid @RequestBody BookmarkRegisterDTO.BookmarkRegisterRequestDTO request) {

        BookmarkRegisterDTO.BookmarkRegisterResponseDTO result = bookmarkService.register(userDetails.getUsername(), request);

        return ApiResponse.onSuccess(result);
    }



    /**
     *
     * 등록 확인창 조회 API
     *
     * 이것도 필요한가
     *
     * */



    /**
     * 찜한 아티스트 조회
     *
     * 프로필 사진, 아티스트 이름, 장르
     *
     * */
    @GetMapping("")
    @Operation(summary = "찜한 아티스트 조회 API")
    public ApiResponse<List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO>> getArtist(@AuthenticationPrincipal UserDetails userDetails) {

        List<BookmarkGetArtistDTO.BookmarkGetArtistResponseDTO> result =
                bookmarkService.getArtist(userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }
}
