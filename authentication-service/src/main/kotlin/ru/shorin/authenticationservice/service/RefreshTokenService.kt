package ru.shorin.authenticationservice.service

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import ru.shorin.authenticationservice.model.RefreshToken
import ru.shorin.authenticationservice.model.User
import ru.shorin.authenticationservice.repository.RefreshTokenRepository
import ru.shorin.utils.JwtUtils
import java.sql.Timestamp
import java.time.Instant
import java.util.Date

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,
    val jwtUtils: JwtUtils,
) {
    fun generateToken(user: User): String {
        val token = generateToken()
        val expiresAt = Timestamp.from(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpiresIn()))
        val newRefreshToken =
            RefreshToken(
                token = token,
                user = user,
                expiresAt = expiresAt,
                createdAt = Timestamp.from(Instant.now()),
            )
        refreshTokenRepository.save(newRefreshToken)
        return token
    }

    fun revokeToken(token: String) {
        refreshTokenRepository.revokeToken(token)
    }

    fun findByToken(token: String): RefreshToken? = refreshTokenRepository.findByToken(token)

    private fun generateToken(): String =
        Jwts
            .builder()
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtUtils.getRefreshTokenExpiresIn()))
            .signWith(jwtUtils.getSignInKey(), Jwts.SIG.HS256)
            .compact()
}
