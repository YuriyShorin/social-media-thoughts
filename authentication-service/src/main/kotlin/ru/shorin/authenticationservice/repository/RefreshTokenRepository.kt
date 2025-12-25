package ru.shorin.authenticationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.shorin.authenticationservice.model.RefreshToken
import java.util.UUID

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    fun findByToken(token: String): RefreshToken?

    @Modifying
    @Query(
        value = """
            UPDATE RefreshToken rt
            SET rt.revoked = true 
            WHERE rt.token = :token
            """,
    )
    fun revokeToken(token: String)
}
