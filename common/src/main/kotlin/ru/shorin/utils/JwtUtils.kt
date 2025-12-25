package ru.shorin.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import ru.shorin.properties.JwtProperties
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtils(
    private val jwtProperties: JwtProperties,
) {
    fun validateToken(token: String?): Jwt<*, *>? =
        Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parse(token)

    fun extractAllClaims(token: String?): Claims =
        Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()

    fun extractEmail(token: String?): String = extractAllClaims(token).subject

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenExpired(token: String): Boolean = extractExpiration(token)?.before(Date()) ?: false

    fun getAccessTokenExpiresIn(): Long = jwtProperties.accessTokenExpiresIn ?: 0L

    fun getRefreshTokenExpiresIn(): Long = jwtProperties.refreshTokenExpiresIn ?: 0L

    private fun extractExpiration(token: String): Date? = extractClaim(token, Claims::getExpiration)
}
