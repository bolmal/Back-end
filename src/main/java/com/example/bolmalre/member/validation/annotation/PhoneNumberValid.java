package com.example.bolmalre.member.validation.annotation;

import com.example.bolmalre.member.validation.validator.PasswordPatternValidator;
import com.example.bolmalre.member.validation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberValid {

    String message() default "유효하지 않은 전화번호 형식입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
