package com.example.bolmal.member.web.dto;

import lombok.*;

public class MemberUpdateDTO {


    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberUpdateRequestDTO{

        String name;

    }


    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberUpdateResponseDTO{

        String name;

    }
}
