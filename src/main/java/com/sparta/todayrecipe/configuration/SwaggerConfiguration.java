package com.sparta.todayrecipe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/**")) // 그중 /api/ 인 URL들만 필터링
//                .paths(PathSelectors.ant("/user/signup"))
                .build();
    }