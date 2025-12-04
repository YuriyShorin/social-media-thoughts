package ru.shorin.authenticationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.shorin.authenticationservice.model.User
import java.sql.Timestamp
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?

    fun findByPhone(phone: String): User?

    fun findByNickname(nickname: String): User?

    fun existsUserByEmail(email: String): Boolean

    fun existsUserByPhone(phoneNumber: String): Boolean

    fun existsUsersByNickname(nickname: String): Boolean

    @Modifying
    @Query(
        value = """
            UPDATE User u
            SET u.lastLoginAt = :lastLoginAt
            WHERE u.id = :id
            """,
    )
    fun updateLastLoginAtById(
        id: UUID?,
        lastLoginAt: Timestamp,
    )
}
