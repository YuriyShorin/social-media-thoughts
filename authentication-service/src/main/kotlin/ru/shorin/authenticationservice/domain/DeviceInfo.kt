package ru.shorin.authenticationservice.domain

import ru.shorin.authenticationservice.model.Client
import ru.shorin.authenticationservice.model.Os
import java.util.UUID

data class DeviceInfo(
    val deviceId: UUID,
    val client: Client,
    val os: Os,
    val deviceName: String,
    val country: String,
    val city: String,
)
