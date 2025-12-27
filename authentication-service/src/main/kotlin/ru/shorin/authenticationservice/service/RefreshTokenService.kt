package ru.shorin.authenticationservice.service

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import ru.shorin.authenticationservice.domain.DeviceInfo
import ru.shorin.authenticationservice.mapper.SessionMapper
import ru.shorin.authenticationservice.model.RefreshToken
import ru.shorin.authenticationservice.model.User
import ru.shorin.authenticationservice.repository.RefreshTokenRepository
import ru.shorin.utils.JwtUtils
import java.sql.Timestamp
import java.time.Instant
import java.util.Date
import java.util.UUID

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,
    val sessionMapper: SessionMapper,
    val jwtUtils: JwtUtils,
) {
    fun generateToken(
        user: User,
        deviceInfo: DeviceInfo,
    ): String {
        val token =
            Jwts
                .builder()
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(Date(System.currentTimeMillis() + jwtUtils.getRefreshTokenExpiresIn()))
                .signWith(jwtUtils.getSignInKey(), Jwts.SIG.HS256)
                .compact()
        val expiresAt = Timestamp.from(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpiresIn()))

        val newRefreshToken = sessionMapper.toRefreshToken(token, user, deviceInfo, expiresAt)
        refreshTokenRepository.save(newRefreshToken)

        return token
    }

    fun findByToken(token: String): RefreshToken? = refreshTokenRepository.findByToken(token)

    fun findByDeviceInfo(deviceInfo: DeviceInfo): RefreshToken? =
        refreshTokenRepository.findByDeviceInfo(
            deviceId = deviceInfo.deviceId,
            client = deviceInfo.client,
            os = deviceInfo.os,
            deviceName = deviceInfo.deviceName,
            country = deviceInfo.country,
            city = deviceInfo.city,
        )

    fun findByUserId(userId: UUID?): List<RefreshToken> =
        userId?.let {
            refreshTokenRepository.findByUserId(it)
        } ?: emptyList()

    fun revokeByToken(token: String) =
        refreshTokenRepository.revokeByToken(
            token = token,
            revokedAt = Timestamp.from(Instant.now()),
        )

    fun revokeByUserId(userId: UUID?) =
        userId?.let {
            refreshTokenRepository.revokeByUserId(
                userId = userId,
                revokedAt = Timestamp.from(Instant.now()),
            )
        }
}
