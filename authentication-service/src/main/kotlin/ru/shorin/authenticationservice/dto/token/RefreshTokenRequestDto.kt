package ru.shorin.authenticationservice.dto.token

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import ru.shorin.authenticationservice.dto.common.DeviceInfoDto

@Schema(description = "Запрос для обновления токена")
data class RefreshTokenRequestDto(
    @Schema(description = "Refresh token")
    val refreshToken: String,
    @Schema(description = "Информация об устройстве", required = true, nullable = false)
    @NotBlank(message = "Информация об устройстве не может быть пустой")
    val deviceInfo: DeviceInfoDto,
)
