package ru.shorin.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Бизнес ошибка")
data class BusinessExceptionResponseDto(
    @Schema(description = "Сообщение", required = true, nullable = false)
    val message: String,
)
