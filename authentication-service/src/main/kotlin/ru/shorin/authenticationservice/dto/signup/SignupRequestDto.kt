package ru.shorin.authenticationservice.dto.signup

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "Запрос для регистрации")
data class SignupRequestDto(
    @Schema(description = "Электронная почта", required = true, nullable = false)
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Неверный формат электронной почты")
    val email: String,
    @Schema(description = "Номер телефона", required = true, nullable = false)
    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(
        regexp = "^\\+?[1-9]\\d{0,2} ?\\(?\\d{1,4}?\\)? ?\\d{1,4}[- ]?\\d{1,4}[- ]?\\d{1,9}$",
        message = "Неверный формат номера телефона",
    )
    val phone: String,
    @Schema(description = "Никнейм", required = true, nullable = false)
    @NotBlank(message = "Никнейм не может быть пустым")
    @Size(min = 4, max = 256, message = "Длина никнейма должна быть в интервале от 8 до 256 символов")
    val nickname: String,
    @Schema(description = "Пароль", required = true, nullable = false)
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    val password: String,
    @Schema(description = "Подтверждение пароля", required = true, nullable = false)
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    val passwordConfirmation: String,
)
