package ru.shorin.authenticationservice.mapper

import org.springframework.stereotype.Component
import ru.shorin.authenticationservice.dto.GetUserResponseDto
import ru.shorin.authenticationservice.dto.SignupRequestDto
import ru.shorin.authenticationservice.model.User
import ru.shorin.model.Role
import java.sql.Timestamp
import java.time.Instant

@Component
class UserMapper {
    fun toUser(
        signupRequestDto: SignupRequestDto,
        password: String,
    ) = User(
        id = null,
        email = signupRequestDto.email,
        password = password,
        phone = signupRequestDto.phone,
        nickname = signupRequestDto.nickname,
        role = Role.USER,
        lastLoginAt = null,
        enabled = true,
        expired = false,
        deleted = false,
        createdAt = Timestamp.from(Instant.now()),
        updatedAt = null,
    )

    fun toGetUserResponseDto(user: User) =
        GetUserResponseDto(
            id = user.id,
            email = user.email,
            phone = user.phone,
            nickname = user.nickname,
            createdAt = user.createdAt,
        )
}
