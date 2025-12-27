package ru.shorin.authenticationservice.dto.common

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.util.UUID

@Schema(description = "Информация об устройстве")
data class DeviceInfoDto(
    @Schema(description = "Идентификатор устройства", required = true, nullable = false)
    @NotBlank(message = "Идентификатор устройства не может быть пустым")
    val deviceId: UUID,
    @Schema(description = "Клиент", required = true, nullable = false)
    @NotBlank(message = "Клиент не может быть пустым")
    val client: ClientDto,
    @Schema(description = "Клиент", required = true, nullable = false)
    @NotBlank(message = "Клиент не может быть пустым")
    val os: OsDto,
    @Schema(description = "Операционная система", required = true, nullable = false)
    @NotBlank(message = "Операционная система не может быть пустой")
    val deviceName: String,
    @Schema(description = "IP", required = true, nullable = false)
    @NotBlank(message = "IP не может быть пустым")
    val ip: String,
)
