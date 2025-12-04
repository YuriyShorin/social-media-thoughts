package ru.shorin.authenticationservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.shorin.model.Role
import java.sql.Timestamp
import java.util.UUID

@Table(schema = "security", name = "users")
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID?,
    @Column(name = "email", unique = true, nullable = false, updatable = true)
    val email: String,
    @Column(name = "phone", unique = true, nullable = false, updatable = true)
    val phone: String,
    @Column(name = "nickname", unique = true, nullable = false, updatable = true)
    val nickname: String,
    @Column(name = "password", unique = false, nullable = false, updatable = true)
    private val password: String,
    @Column(name = "role", unique = false, nullable = false, updatable = true)
    val role: Role,
    @Column(name = "last_login_at", unique = false, nullable = true, updatable = true)
    val lastLoginAt: Timestamp,
    @Column(name = "enabled", unique = false, nullable = false, updatable = true)
    val enabled: Boolean,
    @Column(name = "expired", unique = false, nullable = false, updatable = true)
    val expired: Boolean,
    @Column(name = "deleted", unique = false, nullable = false, updatable = true)
    val deleted: Boolean,
    @Column(name = "createdAt", unique = false, nullable = false, updatable = false)
    val createdAt: Timestamp,
    @Column(name = "updatedAt", unique = false, nullable = true, updatable = true)
    val updatedAt: Timestamp?,
) : UserDetails {
    override fun getPassword() = password

    override fun getUsername() = email

    override fun getAuthorities() = listOf(SimpleGrantedAuthority(role.name))

    override fun isAccountNonExpired() = !expired && !deleted

    override fun isEnabled() = enabled && !deleted
}
