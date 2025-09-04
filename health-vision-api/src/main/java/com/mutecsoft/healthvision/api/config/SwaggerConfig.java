package com.mutecsoft.healthvision.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	@Bean
    OpenAPI openAPI() {
		return new OpenAPI()
	            .components(new Components()
	                    .addSecuritySchemes("jwtAuth", new SecurityScheme()
	                            .type(SecurityScheme.Type.HTTP)
	                            .scheme("bearer")
	                            .bearerFormat("JWT")
	                            .in(SecurityScheme.In.HEADER)
	                            .name("Authorization")))
	            .info(apiInfo())
	            .addSecurityItem(new SecurityRequirement().addList("jwtAuth")); //전역 설정
    }
 
    private Info apiInfo() {
        return new Info()
                .title("HelloGlyco Springdoc")
                .description("Springdoc을 사용한 Swagger UI")
                .version("1.0.0");
    }
}
