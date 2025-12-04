package ru.shorin.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ошибка валидации")
data class ValidationExceptionResponseDto(
    @Schema(description = "Поле", required = true, nullable = false)
    val field: String,
    @Schema(description = "Сообщение", required = true, nullable = true)
    val message: String?,
)
