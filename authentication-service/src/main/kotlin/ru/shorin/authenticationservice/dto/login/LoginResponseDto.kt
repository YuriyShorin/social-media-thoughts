package ru.shorin.authenticationservice.dto.login

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ для логина")
data class LoginResponseDto(
    @Schema(description = "Access токен", required = true, nullable = false)
    val accessToken: String,
    @Schema(description = "Refresh токен", required = true, nullable = false)
    val refreshToken: String,
)
