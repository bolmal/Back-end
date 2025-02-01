package com.example.bolmalre.mail.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {

    private String code;  // 사용자가 입력한 인증 코드

}