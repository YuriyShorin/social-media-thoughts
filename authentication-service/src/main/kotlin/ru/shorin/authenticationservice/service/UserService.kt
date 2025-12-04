package ru.shorin.authenticationservice.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.shorin.authenticationservice.dto.GetUserResponseDto
import ru.shorin.authenticationservice.mapper.UserMapper
import ru.shorin.authenticationservice.model.User

@Service
class UserService(
    private val userMapper: UserMapper,
) {
    fun getUser(user: User): ResponseEntity<GetUserResponseDto> {
        val response = userMapper.toGetUserResponseDto(user)
        return ResponseEntity.ok(response)
    }
}
