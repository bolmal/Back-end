package com.example.bolmalre.global.kakaoMap.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class SimpleKaKaoMapSearchResponse {

    private String placeName;
    private String roadAddressName;
    private String x;
    private String y;
    private String categoryGroupName;
    private String placeUrl;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SimpleKaKaoMapSearchListResponse{

        List<SimpleKaKaoMapSearchResponse> result;

        public static SimpleKaKaoMapSearchListResponse of(KaKaoSearchResponse searchResponse){
            List<SimpleKaKaoMapSearchResponse> collect = searchResponse.getDocuments().stream()
                    .map(doc -> new SimpleKaKaoMapSearchResponse(
                            doc.getPlace_name(),
                            doc.getRoad_address_name(),
                            doc.getX(),
                            doc.getY(),
                            doc.getCategory_group_name(),
                            doc.getPlace_url()
                    ))
                    .collect(Collectors.toList());

            return SimpleKaKaoMapSearchListResponse.builder()
                    .result(collect)
                    .build();
        }
    }
}