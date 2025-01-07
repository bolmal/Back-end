package com.example.bolmal.config;



import com.example.bolmal.config.domain.Member;
import com.example.bolmal.config.domain.Role;
import com.example.bolmal.config.domain.Status;
import com.example.bolmal.config.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request){


        /* converter 메서드는 기본적으로 static 메모리를 할당받아 사용하기 때문에
         * bCryptPasswordEncoder를 주입받을 수 없어 회원가입만 컨버터를 사용하지 않겠습니다 */

        Member newMember= Member.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.ROLE_USER)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthDate())
                .email(request.getEmail())
                .status(Status.ACTIVE)
                .gender(request.getGender())
                .build();

        Member savedMember = memberRepository.save(newMember);


        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .MemberId(savedMember.getId())
                .build();

    }
}
