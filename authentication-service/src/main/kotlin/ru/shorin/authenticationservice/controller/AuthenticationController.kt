package ru.shorin.authenticationservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.shorin.authenticationservice.dto.LoginRequestDto
import ru.shorin.authenticationservice.dto.LoginResponseDto
import ru.shorin.authenticationservice.dto.SignupRequestDto
import ru.shorin.authenticationservice.service.AuthenticationService
import ru.shorin.dto.BusinessExceptionResponseDto
import ru.shorin.dto.InternalErrorResponseDto
import ru.shorin.dto.ValidationExceptionResponseDto

@Tag(name = "Authentication", description = "API для аутентификации")
@RequestMapping("/api/v1/authentication")
@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService,
) {
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Пользователь успешно зарегистрирован",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации входных данных",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ValidationExceptionResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Пользователь уже существует",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BusinessExceptionResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка сервера",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = InternalErrorResponseDto::class),
                    ),
                ],
            ),
        ],
    )
    @PostMapping("/signup")
    fun signup(
        @RequestBody signupRequestDto: SignupRequestDto,
    ): ResponseEntity<Void> = authenticationService.signup(signupRequestDto)

    @Operation(summary = "Вход")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Вход успешно произведен",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации входных данных",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ValidationExceptionResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Неверный пароль",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BusinessExceptionResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Не найден пользователь",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BusinessExceptionResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Внутренняя ошибка сервера",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = InternalErrorResponseDto::class),
                    ),
                ],
            ),
        ],
    )
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDto: LoginRequestDto,
    ): ResponseEntity<LoginResponseDto> = authenticationService.login(loginRequestDto)
}
