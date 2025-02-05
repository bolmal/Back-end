package com.example.bolmalre.member.util;

import com.example.bolmalre.common.apiPayLoad.code.status.ErrorStatus;
import com.example.bolmalre.common.apiPayLoad.exception.handler.MemberHandler;
import com.example.bolmalre.member.web.dto.KakaoDTO;
import com.example.bolmalre.member.web.dto.NaverDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
public class NaverUtil {


    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String client;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirect;


    public NaverDTO.OAuthToken requestToken(String accessToken){


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("client_secret", clientSecret); // client_secret 추가
        params.add("redirect_uri", redirect);
        params.add("code", accessToken);


        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        System.out.println("Naver API Response: " + response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        NaverDTO.OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), NaverDTO.OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new MemberHandler(ErrorStatus.MEMBER_OAUTH_FAIL);
        }
        return oAuthToken;

    }


    public NaverDTO.NaverProfile requestProfile(NaverDTO.OAuthToken oAuthToken){
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();

        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = restTemplate2.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class);

        NaverDTO.NaverProfile naverProfile = null;

        try {
            naverProfile = objectMapper.readValue(response2.getBody(), NaverDTO.NaverProfile.class);
        } catch (JsonProcessingException e) {
            log.info(Arrays.toString(e.getStackTrace()));
            throw new MemberHandler(ErrorStatus.MEMBER_OAUTH_FAIL);
        }

        return naverProfile;
    }
}
