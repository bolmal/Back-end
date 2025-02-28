package com.example.bolmalre.concert.web.controller;

import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.concert.domain.enums.SortType;
import com.example.bolmalre.concert.web.dto.ConcertDetailPageDTO;
import com.example.bolmalre.concert.web.dto.ConcertPageDTO;
import com.example.bolmalre.concert.web.port.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "콘서트 페이지")
    @GetMapping("/")
    public ApiResponse<Page<ConcertPageDTO.ConcertInfoDTO>> getConcertInfos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "TICKET_OPEN") SortType sortType
    ) {
        Page<ConcertPageDTO.ConcertInfoDTO> result = concertService.getConcertPageInfo(page, sortType);
        return ApiResponse.onSuccess(result);
    }

}
