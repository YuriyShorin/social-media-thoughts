package ru.shorin.authenticationservice.dto.token

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ на обновление токена")
data class RefreshTokenResponseDto(
    @Schema(description = "Access токен", required = true, nullable = false)
    val accessToken: String,
    @Schema(description = "Refresh токен", required = true, nullable = false)
    val refreshToken: String,
)
