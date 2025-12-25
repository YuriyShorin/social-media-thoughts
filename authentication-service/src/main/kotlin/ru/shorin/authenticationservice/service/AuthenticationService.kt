package ru.shorin.authenticationservice.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.shorin.authenticationservice.dto.login.LoginRequestDto
import ru.shorin.authenticationservice.dto.login.LoginResponseDto
import ru.shorin.authenticationservice.dto.signup.SignupRequestDto
import ru.shorin.authenticationservice.dto.token.RefreshRequestDto
import ru.shorin.authenticationservice.dto.token.RefreshTokenResponseDto
import ru.shorin.authenticationservice.mapper.UserMapper
import ru.shorin.authenticationservice.model.User
import ru.shorin.authenticationservice.repository.UserRepository
import ru.shorin.exception.BusinessException
import ru.shorin.exception.BusinessExceptionEnum
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime

@Service
class AuthenticationService(
    private val refreshTokenService: RefreshTokenService,
    private val accessTokenService: AccessTokenService,
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
) {
    @Transactional
    fun signup(signupRequestDto: SignupRequestDto): ResponseEntity<Void> {
        if (signupRequestDto.password != signupRequestDto.passwordConfirmation) {
            throw BusinessException(BusinessExceptionEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD)
        }

        if (userRepository.existsUserByEmail(signupRequestDto.email)) {
            throw BusinessException(BusinessExceptionEnum.EMAIL_ALREADY_EXISTS)
        }

        if (userRepository.existsUserByPhone(signupRequestDto.phone)) {
            throw BusinessException(BusinessExceptionEnum.PHONE_ALREADY_EXISTS)
        }

        if (userRepository.existsUsersByNickname(signupRequestDto.nickname)) {
            throw BusinessException(BusinessExceptionEnum.NICKNAME_ALREADY_EXISTS)
        }

        val password =
            passwordEncoder
                .encode(signupRequestDto.password) ?: ""

        val user = userMapper.toUser(signupRequestDto, password)
        userRepository.save(user)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        if (loginRequestDto.login.contains("@")) {
            return loginByEmail(loginRequestDto.login, loginRequestDto.password)
        }

        if (loginRequestDto.login.contains("+")) {
            return loginByPhone(loginRequestDto.login, loginRequestDto.password)
        }

        return loginByNickname(loginRequestDto.login, loginRequestDto.password)
    }

    @Transactional
    fun refresh(refreshRequestDto: RefreshRequestDto): ResponseEntity<RefreshTokenResponseDto> {
        val refreshToken =
            refreshTokenService
                .findByToken(refreshRequestDto.refreshToken)
                ?: throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)

        if (refreshToken.revoked) {
            throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)
        }

        if (refreshToken.expiresAt < Timestamp.from(Instant.now())) {
            throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)
        }

        refreshTokenService.revokeToken(refreshToken.token)

        val (accessToken, newRefreshToken) = generateTokens(refreshToken.user)

        return ResponseEntity
            .ok()
            .body(RefreshTokenResponseDto(accessToken, newRefreshToken))
    }

    private fun loginByEmail(
        email: String,
        password: String,
    ): ResponseEntity<LoginResponseDto> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                password,
            ),
        )

        val user =
            userRepository.findByEmail(email)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_EMAIL)
        userRepository.updateLastLoginAtById(user.id, Timestamp.valueOf(LocalDateTime.now()))

        val (accessToken, refreshToken) = generateTokens(user)

        return ResponseEntity
            .ok()
            .body(LoginResponseDto(accessToken, refreshToken))
    }

    private fun loginByPhone(
        phone: String,
        password: String,
    ): ResponseEntity<LoginResponseDto> {
        val user =
            userRepository.findByPhone(phone)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_PHONE)

        return loginByEmail(user.email, password)
    }

    private fun loginByNickname(
        nickname: String,
        password: String,
    ): ResponseEntity<LoginResponseDto> {
        val user =
            userRepository.findByNickname(nickname)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_NICKNAME)

        return loginByEmail(user.email, password)
    }

    private fun generateTokens(user: User): Pair<String, String> {
        val accessToken = accessTokenService.generateToken(user)
        val refreshToken = refreshTokenService.generateToken(user)

        return accessToken to refreshToken
    }
}
