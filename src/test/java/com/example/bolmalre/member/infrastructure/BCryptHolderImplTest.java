package com.example.bolmalre.member.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BCryptHolderImplTest {


    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private BCryptHolderImpl bCryptHolder;




    @Test
    @DisplayName("encode()를 이용하여 비밀번호를 해싱 할 수 있다")
    public void encode_success(){
        //given
        String password = "testPassword";
        String encodedPassword = "encodedPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

        //when
        String result = bCryptHolder.encode(password);

        //then
        assertThat(result).isEqualTo(encodedPassword);
        Mockito.verify(bCryptPasswordEncoder, times(1)).encode(password);
    }


    @Test
    @DisplayName("matches()를 이용하여 비밀번호 일치 여부를 확인할 수 있다")
    public void matches_success() {
        //given
        String rawPassword = "testPassword";
        String encodedPassword = "encodedPassword";
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        //when
        boolean result = bCryptHolder.matches(rawPassword, encodedPassword);

        //then
        assertThat(result).isTrue();
        Mockito.verify(bCryptPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }


    @Test
    @DisplayName("비밀번호가 일치하지 않으면 false를 반환한다")
    public void matches_fail(){
        //given
        String rawPassword = "testPassword";
        String encodedPassword = "encodedPassword";
        when(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        //when
        boolean result = bCryptHolder.matches(rawPassword, encodedPassword);

        //then
        assertThat(result).isFalse();
        Mockito.verify(bCryptPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}