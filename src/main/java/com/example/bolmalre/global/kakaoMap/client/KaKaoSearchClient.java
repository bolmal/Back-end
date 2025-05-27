package com.example.bolmalre.global.kakaoMap.client;

import com.example.bolmalre.global.config.KaKaoFeignConfig;
import com.example.bolmalre.global.kakaoMap.controller.dto.KaKaoSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "kakaoSearchClient",
        url = "https://dapi.kakao.com",
        configuration = KaKaoFeignConfig.class
)
public interface KaKaoSearchClient {

    /**
     * 1. 공연장 장소를 searchKeyword() 로 찾음
     * 2. 그리고 그 중 장소를 선택하고
     * 3. 그 장소를 https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord 를 통해 좌표로 변환함
     * 4. 그리고 그 좌표를 searchByCategory() 에 넣어서 가져와야함
     * */


    @GetMapping("/v2/local/search/keyword.json")
    KaKaoSearchResponse searchKeyword(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    );


    @GetMapping("/v2/local/search/category.json")
    KaKaoSearchResponse searchByCategory(
            @RequestParam("category_group_code") String categoryGroupCode,
            @RequestParam("x") double x,
            @RequestParam("y") double y,
            @RequestParam("radius") int radius,
            @RequestParam(value = "sort", defaultValue = "distance") String sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    );
}
