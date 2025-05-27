package com.example.bolmalre.domain.member.web.dto;

import com.example.bolmalre.domain.member.validation.annotation.UsernamePatternValid;
import lombok.*;

public class MemberUsernameValidDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberUsernameValidRequestDTO{

        @UsernamePatternValid
        String username;
    }
}
