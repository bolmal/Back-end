package com.example.bolmalre.member.web.dto;

import lombok.Getter;

@Getter
public class NaverDTO {

    @Getter
    public static class OAuthToken {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }

    @Getter
    public static class NaverProfile {
        private String id; // String 타입으로 변경
        private String connected_at;
        private Properties properties;
        private NaverAccount naver_account;
    }

    @Getter
    public static class Properties {
        private String nickname;
    }

    @Getter
    public static class NaverAccount {
        private String email;
        private Boolean is_email_verified;
        private Boolean has_email;
        private Boolean profile_nickname_needs_agreement;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Profile profile;
    }

    @Getter
    public static class Profile {
        private String nickname;
        private Boolean is_default_nickname;
    }
}
