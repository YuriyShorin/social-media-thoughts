package ru.shorin.authenticationservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.sql.Timestamp
import java.util.UUID

@Entity
@Table(name = "RefreshTokens", schema = "Security")
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID? = null,
    @Column(name = "token", unique = true, nullable = false, updatable = false)
    val token: String,
    @ManyToOne
    @JoinColumn(name = "user_id", unique = false, nullable = false, updatable = false)
    val user: User,
    @Column(name = "device_id", unique = false, nullable = false, updatable = false)
    val deviceId: UUID,
    @Column(name = "client", unique = false, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    val client: Client,
    @Column(name = "os", unique = false, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    val os: Os,
    @Column(name = "device_name", unique = false, nullable = false, updatable = false)
    val deviceName: String,
    @Column(name = "city", unique = false, nullable = false, updatable = false)
    val city: String,
    @Column(name = "country", unique = false, nullable = false, updatable = false)
    val country: String,
    @Column(name = "expires_at", unique = false, nullable = false, updatable = false)
    val expiresAt: Timestamp,
    @Column(name = "revoked", unique = false, nullable = false, updatable = true)
    val revoked: Boolean = false,
    @Column(name = "revoked_at", unique = false, nullable = false, updatable = true)
    val revokedAt: Timestamp? = null,
    @Column(name = "created_at", unique = false, nullable = false, updatable = false)
    val createdAt: Timestamp,
)
