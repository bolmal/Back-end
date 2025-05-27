package com.example.bolmalre.domain.member.web.dto;

import com.example.bolmalre.domain.member.validation.annotation.PhoneNumberValid;
import com.example.bolmalre.domain.member.validation.annotation.UsernamePatternValid;
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

        @UsernamePatternValid
        String username;

    }
}
