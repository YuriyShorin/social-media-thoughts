package ru.shorin.authenticationservice.model.domain

import java.util.UUID

data class DeviceInfo(
    val deviceId: UUID,
    val client: Client,
    val os: Os,
    val deviceName: String,
    val country: String,
    val city: String,
)