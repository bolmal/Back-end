package com.example.bolmalre.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ImageHandler;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private MemberProfileImage testMemberProfileImage;



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
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build();

        testMemberProfileImage = MemberProfileImage.builder()
                .imageLink("test")
                .imageName("test")
                .fileName("test")
                .member(testMember)
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
        testMember.setMemberProfileImages(List.of(testMemberProfileImage));

        Member member = spy(Member.builder()
                .id(1L)
                .username("test1234")
                .password("Test123!")
                .name("test")
                .role(Role.ROLE_USER)
                .phoneNumber("010-1234-5678")
                .birthday(LocalDate.of(1995, 5, 20))
                .email("test@example.com")
                .status(Status.ACTIVE)
                .gender(Gender.MALE)
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build());

        // getMemberProfileImages가 빈 리스트를 반환하도록 설정
        when(member.getMemberProfileImages()).thenReturn(new ArrayList<>());

        String dirName = "profile";
        List<MultipartFile> files = Collections.singletonList(testFile);
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile/test.jpg";

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(null);
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL(expectedUrl));

        MemberProfileImage expectedImage = MemberProfileImage.builder()
                .imageLink(expectedUrl)
                .fileName("profile/test.jpg")
                .imageName("test.jpg")
                .member(member)
                .build();

        when(memberProfileImageRepository.saveAll(any())).thenReturn(Collections.singletonList(expectedImage));

        // when
        List<String> result = memberProfileImageService.uploadImages(files, dirName, "test1234");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(expectedUrl);

        verify(memberRepository).findByUsername("test1234");
        verify(memberProfileImageRepository).saveAll(any());
    }


    @Test
    @DisplayName("이미지 업로드 시 파일이 2개 이상일 경우 정해진 오류를 반환한다")
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
    @DisplayName("존재하지 않는 사용자로 이미지 업로드 시도 시 정해진 오류를 반환한다")
    void uploadImages_UserNotFound() {
        // given
        List<MultipartFile> files = Collections.singletonList(testFile);
        String dirName = "profile";

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberProfileImageService.deleteImage("error_username"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("이미 프로필 사진이 등록된 회원이 이미지를 등록하면 오류를 반환한다")
    public void uploadImages_ImageExists(){
        //given
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/profile/test.jpg";
        String dirName = "profile";

        // Member를 spy로 생성
        Member spyMember = spy(testMember);

        MemberProfileImage existImage = MemberProfileImage.builder()
                .imageLink(expectedUrl)
                .fileName("profile/test.jpg")
                .imageName("test.jpg")
                .member(spyMember)
                .build();

        List<MultipartFile> files = Collections.singletonList(testFile);
        List<MemberProfileImage> expectedImages = Collections.singletonList(existImage);

        when(spyMember.getMemberProfileImages()).thenReturn(expectedImages);
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(spyMember));

        // when & then
        assertThatThrownBy(() -> memberProfileImageService.uploadImages(files, dirName, "test123"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_IMAGE_EXIST);
    }


    @Test
    @DisplayName("deleteImage() 를 이용하여 프로필 이미지를 삭제할 수 있다")
    public void deleteImage_Success() throws FileNotFoundException {
        // given
        List<MemberProfileImage> profileImages = new ArrayList<>(List.of(testMemberProfileImage));
        testMember.setMemberProfileImages(new ArrayList<>(profileImages)); // 리스트 복사 후 설정

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(testMember));
        when(memberProfileImageRepository.findByMember(any())).thenReturn(new ArrayList<>(profileImages)); // 리스트 복사하여 반환
        doNothing().when(amazonS3).deleteObject(anyString(), anyString());
        doNothing().when(memberProfileImageRepository).delete(any());
        when(memberRepository.save(any())).thenReturn(testMember);

        // when
        memberProfileImageService.deleteImage("test123");

        // then
        verify(memberRepository, times(2)).findByUsername("test123");
        verify(memberProfileImageRepository, times(1)).findByMember(testMember);
        verify(amazonS3, times(1)).deleteObject("test-bucket", "test");
        verify(memberProfileImageRepository, times(1)).delete(any(MemberProfileImage.class));
        verify(memberRepository, times(1)).save(testMember);
    }


    @Test
    @DisplayName("존재하지 않는 회원의 사진을 삭제하면 정해진 오류를 반환한다")
    public void title(){
        //given
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberProfileImageService.deleteImage("error_username"))
                .isInstanceOf(MemberHandler.class)
                .hasFieldOrPropertyWithValue("code", MEMBER_NOT_FOUND);
    }


    @Test
    @DisplayName("회원의 프로필 사진이 존재하지 않는데 삭제하면 정해진 오류를 반환한다")
    public void deleteImage_ImageNotFound() throws FileNotFoundException {
        //given
        Member member = spy(Member.builder()
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
                .alarmAccount(0)
                .bookmarkAccount(0)
                .subStatus(SubStatus.UNSUBSCRIBE)
                .build());

        List<MemberProfileImage> profileImages = new ArrayList<>();
        doReturn(profileImages).when(member).getMemberProfileImages();

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> memberProfileImageService.deleteImage("test123"))
                .isInstanceOf(ImageHandler.class)
                .hasFieldOrPropertyWithValue("code", IMAGE_NOT_FOUND);
    }





}