package ru.shorin.authenticationservice.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info =
        Info(
            title = "Authentication Service",
            version = "1.0",
            description = "Сервис аутентификации",
        ),
)
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        return OpenAPI()
            .addSecurityItem(
                SecurityRequirement()
                    .addList(securitySchemeName),
            ).components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"),
                ),
            )
    }
}
