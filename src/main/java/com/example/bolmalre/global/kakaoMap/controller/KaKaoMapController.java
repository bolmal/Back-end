package com.example.bolmalre.global.kakaoMap.controller;


import com.example.bolmalre.global.kakaoMap.controller.dto.KaKaoAddressResponse;
import com.example.bolmalre.global.kakaoMap.controller.dto.SimpleKaKaoAddressResponse;
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


    @GetMapping("/kakao-map/search/fixed")
    @Operation(summary = "공연장 주변 고정 정보 조회 API",
            description = "공연장 **주변시설**을 조회합니다 <br><br>" +
                    "편의점, 주차장, 지하철 역, 음식점, 숙박업에 대한 공연장 주변 시설을 조회합니다. <br>")
    @Parameter(name = "keyword",description = "공연장 장소를 입력해주세요")
    public List<SimpleKaKaoMapSearchResponse.SimpleKaKaoMapSearchListResponse> searchList(@RequestParam String keyword){

        return placeSearchService.searchList(keyword);
    }


    @GetMapping("/kakao-map/search/address")
    @Operation(summary = "장소 상세 정보 조회 API")
    @Parameter(name = "location", description = "조회하고 싶은 장소를 입력해주세요")
    public KaKaoAddressResponse.Document searchAddress(@RequestParam String location){
        return placeSearchService.searchAddress(location);
    }


    @GetMapping("/kakao-map/search/coordinate")
    @Operation(summary = "장소 좌표 정보 조회 API")
    @Parameter(name = "location", description = "조회하고 싶은 장소를 입력해주세요")
    public SimpleKaKaoAddressResponse searchCoordinate(@RequestParam String location){
        return placeSearchService.searchSimpleAddress(location);
    }
}
