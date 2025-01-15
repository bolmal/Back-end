package com.example.bolmal.member.web.dto;

import lombok.*;

public class MemberFindUsernameDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberFindUsernameRequestDTO{

        String name;

        String phoneNumber;

    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberFindUsernameResponseDTO{

        Long memberId;

        String username;

    }
}
