package com.tangue.cpw.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    //分组可加多个更改文档显示
    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("demo-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth-api")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi enterApi() {
        return GroupedOpenApi.builder()
                .group("enter-api")
                .pathsToMatch("/clinicalPathway/**")
                .build();
    }
}
