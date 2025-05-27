package com.example.bolmalre.global.kakaoMap.controller;


import com.example.bolmalre.global.kakaoMap.controller.dto.SimpleKaKaoMapSearchResponse;
import com.example.bolmalre.global.kakaoMap.service.PlaceSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 지도 API")
public class KaKaoMapController {

    private final PlaceSearchService placeSearchService;

    @GetMapping("/kakao-map/search")
    @Operation(summary = "공연장 주변 정보 조회 API",
            description = "공연장 **주변시설**을 조회합니다 <br><br>" +
                    "카테고리의 코드를 입력하여 조회하고 싶은 주변 장소의 카테고리를 정할 수 있습니다. <br>" +
                    "코드는 슬랙을 참고해주세요")
    @Parameter(name = "keyword",description = "공연장 장소를 입력해주세요")
    @Parameter(name = "category_group_code",description = "공연장 주변 조회하고 싶은 정보의 카테고리를 입력해주세요")
    public SimpleKaKaoMapSearchResponse.SimpleKaKaoMapSearchListResponse search(@RequestParam String keyword,
                                                     @RequestParam(name = "category_group_code") String categoryGroupCode) {
        return placeSearchService.search(keyword, categoryGroupCode);
    }
}
