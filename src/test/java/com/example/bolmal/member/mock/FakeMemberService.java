package com.example.bolmal.member.mock;

import com.example.bolmal.member.domain.Member;
import com.example.bolmal.member.domain.enums.Role;
import com.example.bolmal.member.domain.enums.Status;
import com.example.bolmal.member.web.dto.MemberJoinDTO;
import com.example.bolmal.member.web.dto.MemberUpdateDTO;
import com.example.bolmal.member.web.port.MemberService;

public class FakeMemberService implements MemberService {

    FakeMemberRepository fakeMemberRepository= new FakeMemberRepository();
    FakeBCrypt fakeBCrypt = new FakeBCrypt();

    @Override
    public MemberJoinDTO.MemberJoinResponseDTO joinMember(MemberJoinDTO.MemberJoinRequestDTO request) {

        /**

         이렇게하면 service 단에서 진행하는 검증이 하나도 적용이 안되잖아요
         -> 검증로직이 업데이트 되어도 서비스 테스트에서 잡을 수 없지 않습니까?

         라면 그건 상관없는 문제라고 생각합니다
         왜냐면 그 부분은 서비스 코드 테스트에서 잡힐테니까요. 우린 서비스코드만 수정하면 됩니다
         그게 단위테스트의 장점이라고 생각합니다.

          */
        fakeMemberRepository.save(Member.builder()
                .id(2L)
                .username(request.getUsername())
                .password(fakeBCrypt.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .role(Role.ROLE_USER)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthDate())
                .email(request.getEmail())
                .status(Status.ACTIVE)
                .gender(request.getGender())
                .build());

        return MemberJoinDTO.MemberJoinResponseDTO.builder()
                .memberId(2L)
                .build();
    }

    // Controller 테스트 코드 작성 시 필요함 FIXME
    @Override
    public MemberUpdateDTO.MemberUpdateResponseDTO update(MemberUpdateDTO.MemberUpdateRequestDTO request,
                                                          String username) {
        return null;
    }
}
