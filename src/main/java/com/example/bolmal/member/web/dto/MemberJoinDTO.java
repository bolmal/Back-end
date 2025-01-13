package com.example.bolmal.member.web.dto;

import com.example.bolmal.member.domain.enums.Gender;
import com.example.bolmal.member.validation.annotation.UsernameDuplicate;
import com.example.bolmal.member.validation.annotation.UsernamePatternValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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

        @Schema(description = "비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z\\d!@#$%^&*(),.?\":{}|<>]{8,12}$",
                message = "비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password;

        @Schema(description = "회원이름 입니다")
        String name;

        @Schema(description = "회원 성별 입니다")
        Gender gender;

        @Schema(description = "회원 닉네임 입니다")
        String nickname;

        @Schema(description = "회원 생일 입니다")
        LocalDate birthDate;

        @Email
        @Schema(description = "회원이메일 입니다")
        String email;

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


}


