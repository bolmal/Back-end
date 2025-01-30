package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.validation.annotation.PhoneNumberValid;
import lombok.*;

public class MemberFindUsernameDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberFindUsernameRequestDTO{

        String name;

        @PhoneNumberValid
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
