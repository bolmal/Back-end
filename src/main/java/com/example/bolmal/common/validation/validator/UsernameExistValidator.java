package com.example.bolmal.common.validation.validator;

import com.example.bolmal.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmal.common.validation.annotation.UsernameExist;
import com.example.bolmal.member.service.port.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernameExistValidator implements ConstraintValidator<UsernameExist, String> {

    private final MemberRepository memberRepository;

    @Override
    public void initialize(UsernameExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        boolean isValid = memberRepository.existsByUsername(username);

        if (isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.MEMBER_USERNAME_DUPLICATE.getMessage()).addConstraintViolation();
        }

        return !isValid;

    }
}
