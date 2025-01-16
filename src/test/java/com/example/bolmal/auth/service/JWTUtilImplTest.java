package com.example.bolmal.auth.service;

import com.example.bolmal.BolmalApplication;
import com.example.bolmal.auth.jwt.JWTUtilImpl;
import com.example.bolmal.mail.service.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BolmalApplication.class)
@ActiveProfiles("test")
public class JWTUtilImplTest {

    @Autowired
    private JWTUtilImpl jwtUtil;

    @MockBean
    private MailServiceImpl mailService;

    @Test
    public void testJwtSecretValue() {

        //secret이 불러와지지 않는 오류가 있어서 테스트 코드를 생성하였습니다

        assertNotNull(jwtUtil);
    }
}
