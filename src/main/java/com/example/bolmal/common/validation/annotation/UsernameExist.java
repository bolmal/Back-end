package com.example.bolmal.common.validation.annotation;

import com.example.bolmal.common.validation.validator.UsernameDuplicateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameDuplicateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameExist {

    String message() default "존재하지 않는 username입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
