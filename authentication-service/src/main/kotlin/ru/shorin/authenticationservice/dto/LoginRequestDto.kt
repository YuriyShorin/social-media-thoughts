package ru.shorin.authenticationservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Запрос для логина")
data class LoginRequestDto(
    @Schema(description = "Логин", required = true, nullable = false)
    @NotBlank(message = "Логин не может быть пустым")
    val login: String,
    @Schema(description = "Пароль", required = true, nullable = false)
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    val password: String,
)
