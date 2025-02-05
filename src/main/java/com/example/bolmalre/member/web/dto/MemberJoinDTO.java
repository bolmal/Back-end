package com.example.bolmalre.member.web.dto;

import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.validation.annotation.PasswordPatternValid;
import com.example.bolmalre.member.validation.annotation.PhoneNumberValid;
import com.example.bolmalre.member.validation.annotation.UsernameDuplicate;
import com.example.bolmalre.member.validation.annotation.UsernamePatternValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

public class MemberJoinDTO {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberJoinRequestDTO{

        @UsernameDuplicate
        @UsernamePatternValid
        @Schema(description = "ID입니다 <br> 영문 소문자, 숫자로 4~16자로 구성")
        String username;

        @PasswordPatternValid
        @Schema(description = "비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password;

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


        // --- 회원 약관동의 ---

        @Schema(description = "서비스 약관동의 여부 입니다")
        Boolean serviceAgreement;

        @Schema(description = "개인정보 수집 여부 입니다")
        Boolean privacyAgreement;

        @Schema(description = "금융거래 정보 약관동의 여부 입니다")
        Boolean financialAgreement;

        @Schema(description = "광고성 정보 수신동의 여부(선택) 입니다")
        Boolean advAgreement;
    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberJoinResponseDTO{

        Long memberId;

    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberOAuthDTO{

        String username;

        String name;

        Role role;

    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberSocialRequestDTO{

        String email;

        String name;

    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberSocialResponseDTO{

        Long memberId;

        String name;

        String upComming;

        Integer alarmCount;

        Integer bookmarkCount;

        boolean isSubscribe;

        String imagePath;
    }

}


