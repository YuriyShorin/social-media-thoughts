package ru.shorin.authenticationservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.shorin.authenticationservice.model.User
import ru.shorin.utils.JwtUtils
import java.util.Date

@Service
class AccessTokenService(
    private val jwtUtils: JwtUtils,
) {
    fun extractEmail(token: String): String = jwtUtils.extractEmail(token)

    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val email = extractEmail(token)
        return (email == userDetails.username) && !isTokenExpired(token)
    }

    fun generateToken(user: User): String = generateToken(HashMap(), user)

    private fun generateToken(
        extraClaims: MutableMap<String, Any>,
        user: User,
    ): String = buildToken(extraClaims, user)

    private fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T = jwtUtils.extractClaim(token, claimsResolver)

    private fun buildToken(
        extraClaims: MutableMap<String, Any>,
        user: User,
    ): String {
        extraClaims["id"] = user.id ?: ""
        extraClaims["phone"] = user.phone
        extraClaims["nickname"] = user.nickname
        extraClaims["role"] = user.role.name

        return Jwts
            .builder()
            .claims(extraClaims)
            .subject(user.email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtUtils.getAccessTokenExpiresIn()))
            .signWith(jwtUtils.getSignInKey(), Jwts.SIG.HS256)
            .compact()
    }

    private fun isTokenExpired(token: String): Boolean =
        extractExpiration(token)
            .before(Date())

    private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)
}
