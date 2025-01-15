package com.example.bolmal.member.web.dto;

import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.validation.annotation.PasswordPatternValid;
import com.example.bolmal.member.validation.annotation.UsernameDuplicate;
import com.example.bolmal.member.validation.annotation.UsernamePatternValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

public class MemberUpdateDTO {


    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberUpdateRequestDTO{

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

    }


    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberUpdateResponseDTO{

        Long memberId;

    }


    @Getter
    public static class MemberPasswordUpdateRequestDTO{

        @PasswordPatternValid
        String newPassword;

    }
}
