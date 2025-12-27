package ru.shorin.authenticationservice.dto.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Клиент")
enum class ClientDto {
    SAFARI,
    CHROME,
    EDGE,
    MOZILLA,
    OPERA,
    YANDEX_BROWSER,
    IOS_APP,
    ANDROID_APP,
}
