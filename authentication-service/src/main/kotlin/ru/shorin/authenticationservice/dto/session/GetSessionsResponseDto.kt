package ru.shorin.authenticationservice.dto.session

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ на получение всех сессий")
data class GetSessionsResponseDto(
    @Schema(description = "Список сессий", required = true, nullable = false)
    val sessions: List<SessionDto>,
)
