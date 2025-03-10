package com.example.bolmalre.common.config;



import com.example.bolmalre.common.auth.filter.CustomLogoutFilter;
import com.example.bolmalre.common.auth.filter.LoginFilter;
import com.example.bolmalre.common.auth.infrastructure.redis.RefreshRepository;
import com.example.bolmalre.common.auth.jwt.JWTFilter;
import com.example.bolmalre.common.auth.jwt.JWTUtilImpl;
import com.example.bolmalre.common.auth.service.RefreshTokenService;
import com.example.bolmalre.member.service.port.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtilImpl jwtUtil;
    private final JWTConfig jwtConfig;
    private final RefreshRepository refreshRepository;
    private final RefreshRepository refreshRedisRepository;
    private final RefreshTokenService refreshTokenService;

    private final MemberRepository memberRepository;



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager Bean 등록, UsernamePasswordAuthenticationFilter에서 필요하기 때문에 생성자로 주입해
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList(
                                "http://localhost:3000",
                                "https://dev.bolmal.shop",
                                "https://bolmal.vercel.app"
                        ));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        // exposedHeaders에 중복 설정 제거하고, 두 개의 헤더를 노출
                        configuration.setExposedHeaders(Arrays.asList("Set-CookieUtil", "access", "Authorization"));

                        return configuration;
                    }
                }));




        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);


        http.authorizeHttpRequests((auth)->auth
                .requestMatchers("/health").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()// Swagger 관련 경로를 허용
                .requestMatchers("members/join","/login","members/usernames","members/passwords","members/valid/usernames").permitAll()
                .requestMatchers("phone-numbers/**").permitAll()
                .requestMatchers("/emails/**").permitAll()
                .requestMatchers("/home/**").permitAll()
                .requestMatchers("concerts/**").permitAll()
                .requestMatchers("/templates/oauth/kakao/callback", "/templates/oauth/naver/callback", "templates/oauth/kakao/front").permitAll() // OAuth 콜백 주소
                .requestMatchers("/oauth/**","/save").permitAll()

                .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil,jwtConfig), LoginFilter.class);

        http.addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, jwtConfig, refreshTokenService,memberRepository), UsernamePasswordAuthenticationFilter.class);


        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRedisRepository,memberRepository), LogoutFilter.class);


        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

