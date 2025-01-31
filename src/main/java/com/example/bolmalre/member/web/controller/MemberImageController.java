package com.example.bolmalre.member.web.controller;



import com.example.bolmalre.common.apiPayLoad.ApiResponse;
import com.example.bolmalre.member.web.port.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/images")
@Slf4j
@Tag(name = "개인 프로필 이미지 관리 API")
public class MemberImageController {


    private final MemberService.MemberProfileImageService memberProfileImageService;


    //이미지 파일들 s3에 저장 후 테이블 추가
    @Operation(summary = "이미지 저장 API", description = "저장해야하는 이미지 파일과 회원정보를 넣어주세요")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> addImages(@RequestPart List<MultipartFile> files,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        String dirName = "member_profile_images";
        List<String> result = memberProfileImageService.uploadImages(files, dirName, userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }

    // 이미지 삭제
    @Operation(summary = "이미지 삭제 API", description = "삭제해야하는 회원ID를 넣어주세요")
    @DeleteMapping("")
    public ApiResponse<String> deleteImage(@AuthenticationPrincipal UserDetails userDetails) throws FileNotFoundException {

        memberProfileImageService.deleteImage(userDetails.getUsername());
        return ApiResponse.onSuccess("Image deleted successfully");
    }

    // 이미지 조회
    @Operation(summary = "단일 이미지 조회 API", description = "조회해야하는 이미지 filename을 넣어주세요")
    @GetMapping("/")
    public ApiResponse<String> getImageByFileName(
            @RequestParam String fileName) {

        String result = memberProfileImageService.findImageByFileName(fileName);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "이미지 조회 API", description = "조회 해야하는 회원Id를 넣어주세요")
    @GetMapping("")
    public ApiResponse<List<String>> getImagesByMealDiaryId(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<String> result = memberProfileImageService.findImagesByUsername(userDetails.getUsername());

        return ApiResponse.onSuccess(result);
    }



}
