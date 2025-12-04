package ru.shorin.model

import java.util.UUID

data class UserAuthentication(
    val id: UUID,
    val email: String,
    val phone: String,
    val nickname: String,
    val role: Role,
)
