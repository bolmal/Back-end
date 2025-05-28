package kr.kakaomap.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaKaoFeignConfig {

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    @Bean
    public RequestInterceptor kakaoRequestInterceptor() {
        return template -> {
            template.header("Authorization", "KakaoAK " + kakaoApiKey);
            template.header("Content-Type", "application/json");
        };
    }
}
