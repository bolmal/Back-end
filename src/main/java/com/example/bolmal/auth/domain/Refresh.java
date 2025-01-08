package com.example.bolmal.auth.domain;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refresh {

    private Long id;

    private String username;

    private String refresh;

    private String expiration;
}
