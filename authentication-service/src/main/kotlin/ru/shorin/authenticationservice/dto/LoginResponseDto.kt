package ru.shorin.authenticationservice.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ для логина")
data class LoginResponseDto(
    @Schema(description = "Токен", required = true, nullable = false)
    val jwtToken: String,
)
