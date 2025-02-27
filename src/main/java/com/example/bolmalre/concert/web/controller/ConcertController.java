package com.example.bolmalre.concert.web.controller;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.port.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
@Tag(name = "concert 페이지 관련 API", description = "홈 화면에 뜨는 콘서트가 아닌 콘서트 페이지에 필요한 API들")
@Builder
public class ConcertController {

    private final ConcertService concertService;


    @Operation(summary = "콘서트 상세 조회 API")
    @GetMapping("/{concertId}")
    public ApiResponse<ConcertDetailPageDTO.ConcertDetailDTO> getConcertDetail(
            @PathVariable Long concertId
    ) {
        ConcertDetailPageDTO.ConcertDetailDTO concertDetail = concertService.getConcertDetailInfo(concertId);

        return ApiResponse.onSuccess(concertDetail);
    }
}
