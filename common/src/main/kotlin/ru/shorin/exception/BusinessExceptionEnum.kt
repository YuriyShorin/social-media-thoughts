package ru.shorin.exception

import org.springframework.http.HttpStatus

enum class BusinessExceptionEnum(
    val message: String,
    val status: HttpStatus,
) {
    // user already exist
    EMAIL_ALREADY_EXISTS("Пользователь с такой электронной почтой уже существует", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS("Пользователь с таким номером телефона уже существует", HttpStatus.CONFLICT),
    NICKNAME_ALREADY_EXISTS("Пользователь с таким никнеймом уже существует", HttpStatus.CONFLICT),

    // password
    PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD("Пароль не совпадает с паролем подтверждения", HttpStatus.BAD_REQUEST),

    // user authentication
    USER_NOT_ENABLED("Пользователь недоступен", HttpStatus.FORBIDDEN),
    USER_EXPIRED("Срок действия пользователя истек", HttpStatus.FORBIDDEN),
    USER_DELETED("Пользователь был удален", HttpStatus.FORBIDDEN),

    // user not found
    USER_NOT_FOUND_BY_PHONE("Пользователь с таким номером телефона не найден", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_BY_EMAIL("Пользователь с такой электронной почтой не найден", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_BY_NICKNAME("Пользователь с таким никнеймом не найден", HttpStatus.NOT_FOUND),

    // authentication
    MISSING_AUTH_HEADER("Отсутствует заголовок авторизации", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS("Неавторизованный доступ к приложению", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("Срок действия токена истек", HttpStatus.UNAUTHORIZED),

    INTERNAL_SERVER_ERROR("Ошибка сервера, обратитесь в техподдержку", HttpStatus.INTERNAL_SERVER_ERROR),
}
