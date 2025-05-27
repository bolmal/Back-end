package com.example.bolmalre.global.kakaoMap.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class KaKaoSearchResponse {
    private List<Document> documents;
    private Meta meta;

    @Getter
    @Setter
    public static class Document {
        private String place_name;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
        private String phone;
        private String category_group_name;
        private String place_url;
    }

    @Getter
    @Setter
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }

}
