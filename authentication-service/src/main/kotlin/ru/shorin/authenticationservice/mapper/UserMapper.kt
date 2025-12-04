package ru.shorin.authenticationservice.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.shorin.authenticationservice.dto.GetUserResponseDto
import ru.shorin.authenticationservice.dto.SignupRequestDto
import ru.shorin.authenticationservice.model.User
import ru.shorin.model.Role
import java.sql.Timestamp
import java.time.LocalDateTime

@Mapper(imports = [Role::class, Timestamp::class, LocalDateTime::class])
interface UserMapper {
    @Mapping(target = "role", expression = "java(Role.USER)")
    @Mapping(target = "createdAt", expression = "java(Timestamp.valueOf(LocalDateTime.now()))")
    @Mapping(target = "lastLoginAt", expression = "java(Timestamp.valueOf(LocalDateTime.now()))")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "expired", constant = "false")
    @Mapping(target = "deleted", constant = "false")
    fun toUser(
        signupRequestDto: SignupRequestDto,
        password: String,
    ): User

    fun toGetUserResponseDto(user: User): GetUserResponseDto
}
