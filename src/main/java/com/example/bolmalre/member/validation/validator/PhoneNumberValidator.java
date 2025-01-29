package com.example.bolmalre.member.validation.validator;

import com.example.bolmalre.member.validation.annotation.PasswordPatternValid;
import com.example.bolmalre.member.validation.annotation.PhoneNumberValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValid, String> {

    private static final String PHONE_PATTERN = "^(?=.*\\d)[\\d\\-]{10,13}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        return value.matches(PHONE_PATTERN);

    }
}
