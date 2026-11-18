
package com.satellite.dashboard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("OrbitLink Satellite Telemetry API")
                        .description(
                                "REST API documentation for the OrbitLink Dashboard. "
                                + "Provides satellite telemetry management, "
                                + "data priority evaluation, transmission "
                                + "management, authentication and history."
                        )
                        .version("1.0.0")
                )

                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
