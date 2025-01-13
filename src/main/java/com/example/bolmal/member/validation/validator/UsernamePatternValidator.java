package com.example.bolmal.member.validation.validator;

import com.example.bolmal.member.validation.annotation.UsernamePatternValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernamePatternValidator implements ConstraintValidator<UsernamePatternValid, String> {

    private static final String USERNAME_PATTERN = "^[a-z0-9]{4,16}$";  // ID 패턴 (영문 소문자, 숫자, 4~16자)

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // null은 다른 어노테이션(@NotNull 등)에서 처리하도록 할 수 있음
        }

        return value.matches(USERNAME_PATTERN);  // 정규식 패턴에 맞는지 확인
    }
}
