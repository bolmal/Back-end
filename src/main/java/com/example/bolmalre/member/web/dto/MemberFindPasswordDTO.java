package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.validation.annotation.PhoneNumberValid;
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

        @PhoneNumberValid
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
