package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.validation.annotation.PasswordPatternValid;
import com.example.bolmalre.member.validation.annotation.PhoneNumberValid;
import com.example.bolmalre.member.validation.annotation.UsernameDuplicate;
import com.example.bolmalre.member.validation.annotation.UsernamePatternValid;
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

        @PhoneNumberValid
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


    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberPasswordUpdateRequestDTO{

        @PasswordPatternValid
        String newPassword;

    }
}
