package ru.shorin.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import ru.shorin.model.Role
import ru.shorin.model.UserAuthentication
import ru.shorin.properties.JwtProperties
import java.util.Date
import java.util.UUID
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

    fun getUserAuthentication(token: String): UserAuthentication {
        val claims = extractAllClaims(token.substring(7))

        val id = UUID.fromString(claims.get("id", String::class.java))
        val email = claims.subject
        val phone = claims.get("phone", String::class.java)
        val nickname = claims.get("nickname", String::class.java)
        val role = claims.get("role", String::class.java)

        return UserAuthentication(id, email, phone, nickname, Role.valueOf(role))
    }

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

    fun getExpiresIn(): Long = jwtProperties.expiresIn ?: 0L

    private fun extractExpiration(token: String): Date? = extractClaim(token, Claims::getExpiration)
}
