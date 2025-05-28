package com.example.bolmalre.global.kakaoMap.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleKaKaoAddressResponse {

    private String y;
    private String x;

    public static SimpleKaKaoAddressResponse of(String y, String x) {
        return new SimpleKaKaoAddressResponse(y, x);
    }

}
