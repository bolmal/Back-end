package com.example.bolmalre.common.auth.service;



import com.example.bolmalre.common.auth.web.dto.CustomUserDetails;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Try to find Member
        Member member = memberRepository.findByUsername(username).orElse(null);

        if (member != null) {
            return new CustomUserDetails(member);
        } throw new UsernameNotFoundException("회원이 존재하지 않습니다" + username);
    }
}