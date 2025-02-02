package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.validation.annotation.PasswordPatternValid;
import lombok.*;

public class MemberPasswordValidDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberPasswordValidRequestDTO{
        @PasswordPatternValid
        String validPassword;
    }
}
