package ru.shorin.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ошибка сервера")
data class InternalErrorResponseDto(
    @Schema(description = "Пользовательское сообщение", required = true, nullable = false)
    val userMessage: String,
    @Schema(description = "Полное сообщение об ошибке", required = true, nullable = true)
    val errorMessage: String?,
)
