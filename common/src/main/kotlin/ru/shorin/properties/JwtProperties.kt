package ru.shorin.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("security.jwt")
data class JwtProperties(
    var secret: String? = null,
    var expiresIn: Long? = null,
)
