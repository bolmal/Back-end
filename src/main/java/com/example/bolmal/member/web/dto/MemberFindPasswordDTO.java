package com.example.bolmal.member.web.dto;

import lombok.*;

public class MemberFindPasswordDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberFindPasswordRequestDTO{

        String username;

        String newPassword;

        String name;

        String phoneNumber;

    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberFindPasswordResponseDTO{

        Long memberId;

        String newPassword;

    }
}
