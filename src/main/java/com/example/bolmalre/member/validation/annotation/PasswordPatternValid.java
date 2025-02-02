package com.example.bolmalre.member.validation.annotation;


import com.example.bolmalre.member.validation.validator.PasswordPatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordPatternValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordPatternValid {

    String message() default "비밀번호는 8~12자의 영문, 숫자, 특수문자를 포함해야 합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}