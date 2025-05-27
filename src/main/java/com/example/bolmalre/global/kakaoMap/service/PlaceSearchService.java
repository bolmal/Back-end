package com.example.bolmalre.global.kakaoMap.service;


import com.example.bolmalre.global.kakaoMap.client.KaKaoSearchClient;
import com.example.bolmalre.global.kakaoMap.controller.dto.KaKaoSearchResponse;
import com.example.bolmalre.global.kakaoMap.controller.dto.SimpleKaKaoMapSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {

    private final KaKaoSearchClient kaKaoSearchClient;

    /**
     * 주어진 키워드로 카카오 API를 통해 장소를 검색합니다.
     *
     * @param keyword 검색할 키워드
     * @return 카카오 API로부터 받은 장소 검색 결과
     */
    public SimpleKaKaoMapSearchResponse.SimpleKaKaoMapSearchListResponse search(String keyword, String categoryGroupCode) {
        // 공연장의 좌표를 가져오기 위해 키워드로 검색
        KaKaoSearchResponse venueResult = kaKaoSearchClient.searchKeyword(
                keyword, 1, 10);

        // 공연장의 좌표 획득
        KaKaoSearchResponse.Document venue = venueResult.getDocuments().get(0);
        double x = Double.parseDouble(venue.getX());
        double y = Double.parseDouble(venue.getY());


        // 공연장 좌표를 기준으로 카테고리 검색 카테고리는 아래 주석 참고
        KaKaoSearchResponse searchResponse = kaKaoSearchClient.searchByCategory(
                categoryGroupCode, x, y, 1000, "distance", 1, 10);

        return SimpleKaKaoMapSearchResponse.SimpleKaKaoMapSearchListResponse.of(searchResponse);

    }

}
