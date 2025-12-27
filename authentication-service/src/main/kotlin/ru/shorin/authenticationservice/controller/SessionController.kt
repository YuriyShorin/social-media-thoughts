package ru.shorin.authenticationservice.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.shorin.authenticationservice.dto.logout.LogoutRequestDto
import ru.shorin.authenticationservice.dto.session.GetSessionsResponseDto
import ru.shorin.authenticationservice.model.User
import ru.shorin.authenticationservice.service.SessionService
import ru.shorin.dto.InternalErrorResponseDto
import ru.shorin.dto.ValidationExceptionResponseDto

@Tag(name = "Session", description = "Управление сессиями")
@RequestMapping("/api/v1/session")
@RestController
class SessionController(
    private val sessionService: SessionService,
) {
    @Operation(summary = "Получить список всех сессий")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Список сессий успешно получен",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetSessionsResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Пользователь не авторизован",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = InternalErrorResponseDto::class),
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
    @GetMapping
    fun getSessions(
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<GetSessionsResponseDto> = sessionService.getSessions(user)

    @Operation(summary = "Выход")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Выход успешно произведен",
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
                description = "Пользователь не авторизован",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = InternalErrorResponseDto::class),
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
    @PostMapping("/logout")
    fun logout(
        @RequestBody logoutRequestDto: LogoutRequestDto,
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<Void> = sessionService.logout(logoutRequestDto, user)

    @Operation(summary = "Выход из всех сессий")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Выход из всех сессий успешно произведен",
            ),
            ApiResponse(
                responseCode = "401",
                description = "Пользователь не авторизован",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = InternalErrorResponseDto::class),
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
    @GetMapping("/logout-all")
    fun logoutAll(
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<Void> = sessionService.logoutAll(user)
}
