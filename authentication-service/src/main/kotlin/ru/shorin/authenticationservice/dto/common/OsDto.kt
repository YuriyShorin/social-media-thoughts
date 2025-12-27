package ru.shorin.authenticationservice.dto.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Операционная система")
enum class OsDto {
    IOS,
    ANDROID,
    MACOS,
    WINDOWS,
    LINUX,
}
