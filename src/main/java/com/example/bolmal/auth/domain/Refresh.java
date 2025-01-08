package com.example.bolmal.auth.domain;

import lombok.*;

import java.util.Date;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refresh {

    private Long id;

    private String username;

    private String refresh;

    private String expiration;


    public static Refresh toRefresh(String username, Integer expireS, String refresh){

        return Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + expireS * 1000L).toString())
                .build();
    }
}
