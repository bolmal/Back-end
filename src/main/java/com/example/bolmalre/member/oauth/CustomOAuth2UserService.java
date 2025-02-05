package com.example.bolmalre.member.oauth;

import com.example.bolmalre.member.converter.MemberConverter;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import com.example.bolmalre.member.infrastructure.UuidHolder;
import com.example.bolmalre.member.web.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UuidHolder uuid;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            /*oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());*/
        }
        else {

            return null;
        }
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Member existData = memberRepository.findByUsername(username)
                .orElseThrow();

        if (existData == null) {

            Member newMember = MemberConverter.toOAuthMember(
                    oAuth2Response.getEmail(),
                    oAuth2Response.getName(),
                    "naver",
                    passwordEncoder,
                    uuid
            );

            memberRepository.save(newMember);

            MemberJoinDTO.MemberOAuthDTO memberDTO = MemberJoinDTO.MemberOAuthDTO.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .build();

            return new CustomOAuth2User(memberDTO);
        }
        else {

            Member.setNameAndEmail(existData,oAuth2Response.getName(),oAuth2Response.getEmail());
            memberRepository.save(existData);

            MemberJoinDTO.MemberOAuthDTO memberDTO = MemberJoinDTO.MemberOAuthDTO.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .build();

            return new CustomOAuth2User(memberDTO);
        }
    }
}