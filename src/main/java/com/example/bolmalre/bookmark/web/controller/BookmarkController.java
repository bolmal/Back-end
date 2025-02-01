package com.example.bolmalre.bookmark.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Tag(name = "찜 기능 API")
@Builder
public class BookmarkController {



    /**
     찜 등록하기

     아티스트를 찜하는 기능입니다
     찜하면 회원의 찜 쿠폰이 하나 줄어들고

     아티스트의 공연이 업로드 될 때마다 그 사람한테 메일을 보내줍니다

     찜 설정이 완료되면 찜 설정확인 메일을 전송합니다
     알림받을 아티스트 정보
     * */



    /**
     *
     * 등록 확인창 조회 API
     *
     * 이것도 필요한가
     *
     * */



    /**
     * 찜한 아티스트 조회
     *
     * 프로필 사진, 아티스트 이름, 장르
     *
     * */
}
