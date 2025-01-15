package com.example.bolmal.member.validation.validator;

import com.example.bolmal.member.validation.annotation.PasswordPatternValid;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordPatternValidator implements ConstraintValidator<PasswordPatternValid, String> {

    private static final String USERNAME_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z\\d!@#$%^&*(),.?\":{}|<>]{8,12}$";  // ID 패턴 (영문 소문자, 숫자, 4~16자)


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.matches(USERNAME_PATTERN);
    }
}
