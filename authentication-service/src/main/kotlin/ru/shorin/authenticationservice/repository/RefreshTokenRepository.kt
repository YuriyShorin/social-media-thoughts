package ru.shorin.authenticationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.shorin.authenticationservice.model.domain.Client
import ru.shorin.authenticationservice.model.domain.Os
import ru.shorin.authenticationservice.model.entity.RefreshToken
import java.sql.Timestamp
import java.util.UUID

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByToken(token: String): RefreshToken?

    @Query(
        value = """
            SELECT rt FROM RefreshToken rt
            WHERE rt.deviceId = :deviceId
            AND rt.client = :client
            AND rt.os = :os
            AND rt.deviceName = :deviceName
            AND rt.country = :country
            AND rt.city = :city
            AND rt.revoked = false
        """,
    )
    fun findByDeviceInfo(
        deviceId: UUID,
        client: Client,
        os: Os,
        deviceName: String,
        country: String,
        city: String,
    ): RefreshToken?

    @Query(
        value = """
            SELECT rt FROM RefreshToken rt
            WHERE rt.user.id = :userId
            AND rt.revoked = false 
        """,
    )
    fun findByUserId(userId: UUID): List<RefreshToken>

    @Modifying
    @Query(
        value = """
            UPDATE RefreshToken rt
            SET rt.revoked = true, rt.revokedAt = :revokedAt
            WHERE rt.token = :token
            """,
    )
    fun revokeByToken(
        token: String,
        revokedAt: Timestamp,
    )

    @Modifying
    @Query(
        value = """
            UPDATE RefreshToken rt
            SET rt.revoked = true, rt.revokedAt = :revokedAt
            WHERE rt.user.id = :userId
            AND rt.revoked = false 
            """,
    )
    fun revokeByUserId(
        userId: UUID,
        revokedAt: Timestamp,
    )

    @Modifying
    @Query(
        value = """
        DELETE FROM RefreshToken rt
        WHERE rt.revoked = true 
        AND rt.revokedAt < :revokedBefore
    """
    )
    fun deleteRevoked(revokedBefore: Timestamp)

    @Modifying
    @Query(
        value = """
        DELETE FROM RefreshToken rt
        WHERE rt.expiresAt < :now
    """
    )
    fun deleteExpired(now: Timestamp)
}
