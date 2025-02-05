package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.validation.annotation.PasswordPatternValid;
import com.example.bolmalre.member.validation.annotation.UsernameDuplicate;
import com.example.bolmalre.member.validation.annotation.UsernamePatternValid;
import lombok.*;

public class MemberUsernameValidDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberUsernameValidRequestDTO{

        @UsernameDuplicate
        @UsernamePatternValid
        String username;
    }
}
