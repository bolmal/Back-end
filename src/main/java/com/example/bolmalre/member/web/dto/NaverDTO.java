package com.example.bolmalre.member.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // 클래스에 없는 필드는 무시
public class NaverDTO {

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OAuthToken {
        private String access_token;
        private String refresh_token;
        private String expires_in;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaverProfile {
        private String resultcode; // Naver API 응답 코드
        private String message;    // Naver API 응답 메시지

        // Naver API의 response 필드를 매핑
        @JsonProperty("response")
        private Response response;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Response {
            private String id;
            private String email;
            private String name;
        }
    }
}
