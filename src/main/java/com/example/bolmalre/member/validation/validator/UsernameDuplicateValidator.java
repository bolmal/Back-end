package com.example.bolmalre.member.validation.validator;


import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.member.service.port.MemberRepository;
import com.example.bolmalre.member.validation.annotation.UsernameDuplicate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsernameDuplicateValidator implements ConstraintValidator<UsernameDuplicate, String> {

    private MemberRepository memberRepository;

    // 기본 생성자 필요 (Spring이 사용할 수 있도록)
    public UsernameDuplicateValidator() {
    }

    @Autowired
    public UsernameDuplicateValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void initialize(UsernameDuplicate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        // username이 이미 존재하는지 확인
        boolean isDuplicate = memberRepository.existsByUsername(username);

        if (isDuplicate) {
            // 중복된 경우, 기본 제약 조건 메시지 비활성화 후, 사용자 정의 메시지 추가
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.MEMBER_USERNAME_DUPLICATE.getMessage())
                    .addConstraintViolation();
        }

        // 중복된 username이 있으면 false 반환 (유효하지 않음)
        return !isDuplicate;
    }
}

