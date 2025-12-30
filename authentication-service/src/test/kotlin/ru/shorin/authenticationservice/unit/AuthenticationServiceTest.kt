package ru.shorin.authenticationservice.unit

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import ru.shorin.authenticationservice.dto.signup.SignupRequestDto
import ru.shorin.authenticationservice.mapper.SessionMapper
import ru.shorin.authenticationservice.mapper.UserMapper
import ru.shorin.authenticationservice.model.User
import ru.shorin.authenticationservice.repository.UserRepository
import ru.shorin.authenticationservice.service.AccessTokenService
import ru.shorin.authenticationservice.service.AuthenticationService
import ru.shorin.authenticationservice.service.GeoIpService
import ru.shorin.authenticationservice.service.RefreshTokenService

@ExtendWith(MockitoExtension::class)
class AuthenticationServiceTest {
    @Mock
    lateinit var refreshTokenService: RefreshTokenService

    @Mock
    lateinit var accessTokenService: AccessTokenService

    @Mock
    lateinit var geoIpService: GeoIpService

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var sessionMapper: SessionMapper

    @Mock
    lateinit var userMapper: UserMapper

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var authenticationManager: AuthenticationManager

    @InjectMocks
    lateinit var authenticationService: AuthenticationService

    @Test
    fun `signup should create user when data is valid`() {
        val dto = validSignupDto()
        val encodedPassword = "encodedPassword"
        val user = mock<User>()

        whenever(userRepository.existsUserByEmail(dto.email)).thenReturn(false)
        whenever(userRepository.existsUserByPhone(dto.phone)).thenReturn(false)
        whenever(userRepository.existsUsersByNickname(dto.nickname)).thenReturn(false)

        whenever(passwordEncoder.encode(dto.password)).thenReturn(encodedPassword)
        whenever(userMapper.toUser(dto, encodedPassword)).thenReturn(user)

        val response = authenticationService.signup(dto)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        verify(userRepository).save(user)
    }

    private fun validSignupDto() =
        SignupRequestDto(
            email = "test@mail.com",
            phone = "+79990000000",
            nickname = "test user",
            password = "password",
            passwordConfirmation = "password",
        )
}
