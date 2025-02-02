package com.example.bolmalre.member.validation.annotation;

import com.example.bolmalre.member.validation.validator.UsernamePatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernamePatternValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernamePatternValid {

    String message() default "Username이 패턴과 일치하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}