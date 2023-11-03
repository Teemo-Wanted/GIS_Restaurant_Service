package com.wanted.restaurant.base.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(title = "restaurant",
                description = "teemO 2nd Project restaurant",
                version = "v1")
)
@Configuration
public class SwaggerConfig {
}
