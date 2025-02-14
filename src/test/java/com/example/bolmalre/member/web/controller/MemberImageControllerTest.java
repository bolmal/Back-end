package com.example.bolmalre.member.web.controller;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.ImageHandler;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.domain.Member;
import com.example.bolmalre.member.domain.enums.Gender;
import com.example.bolmalre.member.domain.enums.Role;
import com.example.bolmalre.member.domain.enums.Status;
import com.example.bolmalre.member.domain.enums.SubStatus;
import com.example.bolmalre.member.service.MemberServiceImpl;
import com.example.bolmalre.member.service.port.MemberProfileImageRepository;
import com.example.bolmalre.member.service.port.MemberRepository;
import com.example.bolmalre.member.web.port.MemberProfileImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class MemberImageControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MemberProfileImageRepository memberProfileImageRepository;

    @Mock
    MemberServiceImpl memberService;

    /**
     *
     * 헐 레전드 사실
     * 여기서 @Mock을 사용하면 실제 API가 호출돼서 S3안에 사진이 실제로 저장되어 버립니다
     * 왜냐면 테스트를 위해서 호출한 API 안에서 실제 MemberProfileImageService가 호출되기 때문입니다
     * @Mock은 스프링 컨텍스트에는 영향을 미치지 못하니까요
     *
     * 그렇기 때문에 @MockBean을 이용하여 스프링 컨텍스트에 프록시 객체를 주입해서
     * 실제 로직이 실행되지 않도록 해야 모킹이 가능해지는 것입니다
     *
     * 이는 반대로 생각하면 통합테스트를 할때 실제 서비스 로직으로 테스트를 하고 싶다면,
     * @MockBean을 사용하면 안된다는걸 의미하겠죠?
     *
     * */
    @MockBean
    MemberProfileImageService memberProfileImageService;

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("test12")
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
                .memberProfileImages(new ArrayList<>())
                .build();

        memberRepository.save(member);
    }


    @Test
    @DisplayName("addImages()를 이용하여 회원의 프로필 이미지를 저장 할 수 있다")
    @WithMockUser(username = "test12", roles = "USER")
    public void addImages_success() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content 1".getBytes()
        );

        List<String> expectedUrls = List.of(
                "https://test-bucket.com/test1.jpg"
        );

        when(memberProfileImageService.uploadImages(
                any(),
                eq("member_profile_images"),
                eq("test12")
        )).thenReturn(expectedUrls);

        // when & then
        mockMvc.perform(multipart("/members/images")
                        .file(file1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").value("https://test-bucket.com/test1.jpg"));
    }


    @Test
    @DisplayName("프로필 이미지를 두 장 이상 입력하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void addImage_fail_image_count() throws Exception {
        // Given
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content 1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "test2.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content 2".getBytes()
        );

        when(memberProfileImageService.uploadImages(
                any(),
                eq("member_profile_images"),
                eq("test12")
        )).thenThrow(new MemberHandler(ErrorStatus.MEMBER_IMAGE_COUNT_ERROR));

        // when & then
        mockMvc.perform(multipart("/members/images")
                        .file(file1)
                        .file(file2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("IMAGE4003"))
                .andExpect(jsonPath("$.message").value("프로필 이미지는 한 장만 등록 가능합니다"));
    }


    @Test
    @DisplayName("이미 프로필 이미지가 등록되어있는데 프로필 이미지를 등록하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void addImage_fail_exist() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content 1".getBytes()
        );

        when(memberProfileImageService.uploadImages(
                any(),
                eq("member_profile_images"),
                eq("test12")
        )).thenThrow(new MemberHandler(ErrorStatus.MEMBER_IMAGE_EXIST));

        // when & then
        mockMvc.perform(multipart("/members/images")
                        .file(file1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("IMAGE4001"))
                .andExpect(jsonPath("$.message").value("프로필 이미지가 이미 등록되어 있습니다"));
    }


    @Test
    @DisplayName("존재하지 않는 회원의 프로필 사진을 등록하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void addImage_fail_memberNotFound() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content 1".getBytes());

        when(memberProfileImageService.uploadImages(
                any(),
                eq("member_profile_images"),
                eq("test12")
        )).thenThrow(new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // when & then
        mockMvc.perform(multipart("/members/images")
                        .file(file1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("이미지 업로드 중 오류가 발생하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void addImage_fail_ImageUploadError() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content 1".getBytes());

        when(memberProfileImageService.uploadImages(
                any(),
                eq("member_profile_images"),
                eq("test12")
        )).thenThrow(new ImageHandler(ErrorStatus.IMAGE_UPLOAD_ERROR));

        //when & then
        mockMvc.perform(multipart("/members/images")
                        .file(file1))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("IMAGE5004"))
                .andExpect(jsonPath("$.message").value("이미지 등록 중 오류가 발생하였습니다"));
    }


    @Test
    @DisplayName("deleteImage()를 이용하여 회원의 프로필 이미지를 삭제 할 수 있다")
    @WithMockUser(username = "test12", roles = "USER")
    public void deleteImage_success() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/members/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."));
    }


    @Test
    @DisplayName("삭제 할 이미지가 없으면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void deleteImage_fail_ImageNotFound() throws Exception {
        //given
        when(memberProfileImageService.deleteImage(
                any()
        )).thenThrow(new ImageHandler(ErrorStatus.IMAGE_NOT_FOUND));

        //when & then
        mockMvc.perform(delete("/members/images"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("IMAGE4002"))
                .andExpect(jsonPath("$.message").value("이미지를 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("존재하지 않는 회원의 이미지를 삭제하려하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void deleteImage_fail_MemberNotFound() throws Exception {
        //given
        when(memberProfileImageService.deleteImage(
                any()
        )).thenThrow(new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        //when & then
        mockMvc.perform(delete("/members/images"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("MEMBER4004"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다"));
    }


    @Test
    @DisplayName("회원의 이미지 삭제 중 오류가 발생하면 정해진 예외를 반환한다")
    @WithMockUser(username = "test12", roles = "USER")
    public void title() throws Exception {
        //given
        when(memberProfileImageService.deleteImage(
                any()
        )).thenThrow(new ImageHandler(ErrorStatus.IMAGE_REMOVE_ERROR));

        //when & then
        mockMvc.perform(delete("/members/images"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("IMAGE5002"))
                .andExpect(jsonPath("$.message").value("이미지 삭제 중 오류가 발생하였습니다"));
    }
}