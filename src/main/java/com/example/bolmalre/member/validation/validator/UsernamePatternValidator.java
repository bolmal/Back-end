package com.example.bolmalre.member.validation.validator;


import com.example.bolmalre.member.validation.annotation.UsernamePatternValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernamePatternValidator implements ConstraintValidator<UsernamePatternValid, String> {

    private static final String USERNAME_PATTERN = "^[a-z0-9]{4,16}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.matches(USERNAME_PATTERN);
    }
}
