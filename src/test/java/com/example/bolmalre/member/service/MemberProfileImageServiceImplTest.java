package com.example.bolmalre.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.bolmalre.member.infrastructure.MemberProfileImageRepository;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberProfileImageServiceImplTest {

    @Mock
    MemberProfileImageRepository memberProfileImageRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AmazonS3 amazonS3;

    @Mock
    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    @InjectMocks
    MemberProfileImageServiceImpl memberProfileImageService;



    @Test
    @DisplayName("uploadImages() 를 이용해서 이미지를 업로드 할 수 있다")
    public void uploadImages_test(){
        //given



        //when

        //then
    }

}