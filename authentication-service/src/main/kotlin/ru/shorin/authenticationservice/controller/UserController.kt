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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.shorin.authenticationservice.dto.GetUserResponseDto
import ru.shorin.authenticationservice.model.User
import ru.shorin.authenticationservice.service.UserService
import ru.shorin.dto.InternalErrorResponseDto

@Tag(name = "User", description = "API для получения данных о пользователе")
@RequestMapping("/api/v1/user")
@RestController
class UserController(
    val userService: UserService,
) {
    @Operation(summary = "Получить информацию об авторизованном пользователе")
    @GetMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Получены данные о пользователе",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetUserResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = """
                    1) Отсутствует заголовок авторизации
                    2) Неавторизованный доступ к приложению
                    3) Срок действия токена истек
                """,
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
    fun getUser(
        @AuthenticationPrincipal user: User,
    ): ResponseEntity<GetUserResponseDto> = userService.getUser(user)
}
