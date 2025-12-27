package ru.shorin.authenticationservice.dto.session

import io.swagger.v3.oas.annotations.media.Schema
import ru.shorin.authenticationservice.dto.common.ClientDto
import ru.shorin.authenticationservice.dto.common.OsDto
import java.time.Instant
import java.util.UUID

@Schema(description = "Сессия")
data class SessionDto(
    @Schema(description = "Идентификатор устройства", required = true, nullable = false)
    val deviceId: UUID,
    @Schema(description = "Клиент", required = true, nullable = false)
    val client: ClientDto,
    @Schema(description = "Клиент", required = true, nullable = false)
    val os: OsDto,
    @Schema(description = "Операционная система", required = true, nullable = false)
    val deviceName: String,
    @Schema(description = "Страна", required = true, nullable = false)
    val country: String,
    @Schema(description = "Город", required = true, nullable = false)
    val city: String,
    @Schema(description = "Дата и время создания", required = true, nullable = false)
    val createdAt: Instant,
)
