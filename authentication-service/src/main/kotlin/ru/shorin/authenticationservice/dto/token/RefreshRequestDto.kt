package ru.shorin.authenticationservice.dto.token

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос для обновления токена")
data class RefreshRequestDto(
    @Schema(description = "Refresh token")
    val refreshToken: String,
)
