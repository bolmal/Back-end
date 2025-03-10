package com.example.bolmalre.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI BOLMAL_API(){
        Info info=new Info()
                .title("볼래말래_API")
                .description("TEAM 청춘연구소 - 볼래말래 API입니다")
                .version("1.0");

        String jwtSchemeName="JWTToken";

        SecurityRequirement securityRequirement=new SecurityRequirement().addList(jwtSchemeName);

        Components components=new Components()
                .addSecuritySchemes(jwtSchemeName,new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);

    }

}