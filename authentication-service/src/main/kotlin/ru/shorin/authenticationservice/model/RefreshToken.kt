package ru.shorin.authenticationservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
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
    @Column(name = "created_at", unique = false, nullable = false, updatable = false)
    val createdAt: Timestamp,
    @Column(name = "expires_at", unique = false, nullable = false, updatable = false)
    val expiresAt: Timestamp,
    @Column(name = "revoked", unique = false, nullable = false, updatable = true)
    var revoked: Boolean = false,
)
