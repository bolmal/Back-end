package com.example.bolmalre.member.web.dto;


import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.validation.annotation.UsernameDuplicate;
import com.example.bolmalre.member.validation.annotation.UsernamePatternValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

public class MemberProfileDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberProfileResponseDTO{

        @UsernameDuplicate
        @UsernamePatternValid
        @Schema(description = "ID입니다 <br> 영문 소문자, 숫자로 4~16자로 구성")
        String username;

        @Schema(description = "회원이름 입니다")
        String name;

        @Schema(description = "회원 성별 입니다")
        Gender gender;

        @Schema(description = "회원 생일 입니다")
        LocalDate birthDate;

        @Email
        @Schema(description = "회원이메일 입니다")
        String email;

        @Schema(description = "회원전화번호 입니다")
        String phoneNumber;

        @Schema(description = "회원 프로필 이미지 입니다")
        String imagePath;

    }
}
