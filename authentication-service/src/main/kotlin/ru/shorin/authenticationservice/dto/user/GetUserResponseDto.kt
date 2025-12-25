package ru.shorin.authenticationservice.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

@Schema(description = "Ответ на получение данных о пользователе")
data class GetUserResponseDto(
    @Schema(description = "Идентификатор", required = true, nullable = false)
    val id: UUID?,
    @Schema(description = "Электронная почта", required = true, nullable = false)
    val email: String,
    @Schema(description = "Номер телефона", required = true, nullable = false)
    val phone: String,
    @Schema(description = "Никнейм", required = true, nullable = false)
    val nickname: String,
    @Schema(description = "Дата создания", required = true, nullable = false)
    val createdAt: Instant,
)
