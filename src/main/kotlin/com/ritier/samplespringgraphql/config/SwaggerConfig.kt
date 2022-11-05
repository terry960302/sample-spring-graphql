package com.ritier.samplespringgraphql.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// https://velog.io/@jeong-god/Spring-boot-Swagger-API-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0
@Configuration
class Swagger2Config {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1-definition")
            .pathsToMatch("/api/**")
            .build();
    }

    @Bean
    fun springOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("my API")
                    .description("프로젝트 API 명세서입니다.")
                    .version("v0.0.1")
            )
    }
}