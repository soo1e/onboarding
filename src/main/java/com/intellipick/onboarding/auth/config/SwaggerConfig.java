package com.intellipick.onboarding.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Intellipick API 문서")
                        .version("1.0")
                        .description("Intellipick 프로젝트의 API 명세서입니다."));
    }
}
