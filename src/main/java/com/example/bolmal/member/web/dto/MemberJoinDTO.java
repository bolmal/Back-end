package com.example.bolmal.member.web.dto;

import com.example.bolmal.member.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

public class MemberJoinDTO {

    @Getter
    public static class MemberJoinRequestDTO{


        @Schema(description = "회원이름 입니다")
        String name;

        @Schema(description = "회원전화번호 입니다")
        String phoneNumber;

        @Schema(description = "회원이메일 입니다")
        String email;

        @Schema(description = "ID입니다 <br> 6~20자의 영문,숫자")
        String username;

        @Schema(description = "비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password;

        @Schema(description = "회원 닉네임 입니다")
        String nickname;

        LocalDate birthDate;

        Gender gender;
    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class MemberJoinResponseDTO{

        Long MemberId;

    }


}


