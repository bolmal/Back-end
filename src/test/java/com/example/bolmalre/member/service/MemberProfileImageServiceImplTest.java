package com.example.bolmalre.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.MemberProfileImage;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.infrastructure.MemberProfileImageRepository;
import com.example.bolmalre.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberProfileImageServiceImplTest {

    @Mock
    MemberProfileImageRepository memberProfileImageRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    @InjectMocks
    MemberProfileImageServiceImpl memberProfileImageService;

    private Member testMember;
    private MockMultipartFile testFile;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(memberProfileImageService, "bucket", "test-bucket");

        testMember = Member.builder()
                .id(1L)
                .username("test123")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .profileImage(null)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        testFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }


    @Test
    @DisplayName("uploadImage()를 통해 단일 이미지를 업로드 할 수 있다")
    void uploadImage_Success() throws IOException {
        // given
        String dirName = "profile";
        String expectedFileName = dirName + "/test.jpg";
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/" + expectedFileName;

        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(null);
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL(expectedUrl));

        // when
        MemberProfileImage result = memberProfileImageService.uploadImage(dirName, testFile, testMember);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getImageLink()).isEqualTo(expectedUrl);
        assertThat(result.getImageName()).isEqualTo("test.jpg");
        assertThat(result.getMember()).isEqualTo(testMember);

        verify(amazonS3).putObject(any(PutObjectRequest.class));
        verify(amazonS3).getUrl(anyString(), anyString());
    }


    @Test
    @DisplayName("uploadImage()를 통해 이미지를 업로드 할 수 있다")
    void uploadImages_Success() throws IOException {
        // given
        String dirName = "profile";
        List<MultipartFile> files = Collections.singletonList(testFile);
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile/test.jpg";

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(testMember));
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(null);
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL(expectedUrl));

        MemberProfileImage expectedImage = MemberProfileImage.builder()
                .imageLink(expectedUrl)
                .fileName("profile/test.jpg")
                .imageName("test.jpg")
                .member(testMember)
                .build();

        when(memberProfileImageRepository.saveAll(any())).thenReturn(Collections.singletonList(expectedImage));

        // when
        List<String> result = memberProfileImageService.uploadImages(files, dirName, "test123");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expectedUrl);

        verify(memberRepository).findByUsername("test123");
        verify(memberProfileImageRepository).saveAll(any());
    }


    @Test
    @DisplayName("이미지 업로드 시 파일이 2개 이상일 경우 정해진 오류가 발생한다")
    void uploadImages_TooManyFiles() {
        // given
        List<MultipartFile> files = List.of(testFile, testFile);
        String dirName = "profile";

        // when & then
        assertThrows(MemberHandler.class, () ->
                memberProfileImageService.uploadImages(files, dirName, "test123")
        );
    }


    @Test
    @DisplayName("존재하지 않는 사용자로 이미지 업로드 시도 시 정해진 오류가 발생한다")
    void uploadImages_UserNotFound() {
        // given
        List<MultipartFile> files = Collections.singletonList(testFile);
        String dirName = "profile";

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                memberProfileImageService.uploadImages(files, dirName, "nonexistent")
        );
    }





}