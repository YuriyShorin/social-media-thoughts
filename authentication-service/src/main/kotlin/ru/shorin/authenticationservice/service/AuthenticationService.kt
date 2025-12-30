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
import ru.shorin.authenticationservice.dto.token.RefreshTokenRequestDto
import ru.shorin.authenticationservice.dto.token.RefreshTokenResponseDto
import ru.shorin.authenticationservice.mapper.SessionMapper
import ru.shorin.authenticationservice.mapper.UserMapper
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
    private val geoIpService: GeoIpService,
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val sessionMapper: SessionMapper,
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

        val password = passwordEncoder.encode(signupRequestDto.password) ?: ""
        val user = userMapper.toUser(signupRequestDto, password)
        userRepository.save(user)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        if (loginRequestDto.login.contains("@")) {
            return loginByEmail(loginRequestDto, loginRequestDto.login)
        }

        if (loginRequestDto.login.contains("+")) {
            return loginByPhone(loginRequestDto)
        }

        return loginByNickname(loginRequestDto)
    }

    @Transactional
    fun refreshToken(refreshTokenRequestDto: RefreshTokenRequestDto): ResponseEntity<RefreshTokenResponseDto> {
        val (county, city) = geoIpService.resolve(refreshTokenRequestDto.deviceInfo.ip)
        val deviceInfo = sessionMapper.toDeviceInfo(refreshTokenRequestDto.deviceInfo, county, city)
        val refreshToken =
            refreshTokenService.findByToken(refreshTokenRequestDto.refreshToken)
                ?: throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)

        if (refreshToken.revoked) {
            throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)
        }

        if (refreshToken.expiresAt < Timestamp.from(Instant.now())) {
            throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)
        }

        refreshTokenService.revokeByToken(refreshToken.token)

        val accessToken = accessTokenService.generateToken(refreshToken.user)
        val newRefreshToken = refreshTokenService.generateToken(refreshToken.user, deviceInfo)

        return ResponseEntity
            .ok()
            .body(RefreshTokenResponseDto(accessToken, newRefreshToken))
    }

    private fun loginByEmail(
        loginRequestDto: LoginRequestDto,
        email: String,
    ): ResponseEntity<LoginResponseDto> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                loginRequestDto.password,
            ),
        )

        val (county, city) = geoIpService.resolve(loginRequestDto.deviceInfo.ip)
        val deviceInfo = sessionMapper.toDeviceInfo(loginRequestDto.deviceInfo, county, city)
        val user =
            userRepository.findByEmail(email)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_EMAIL)
        userRepository.updateLastLoginAtById(user.id, Timestamp.valueOf(LocalDateTime.now()))

        val existsRefreshToken = refreshTokenService.findByDeviceInfo(deviceInfo)
        existsRefreshToken?.let {
            refreshTokenService.revokeByToken(it.token)
        }

        val accessToken = accessTokenService.generateToken(user)
        val newRefreshToken = refreshTokenService.generateToken(user, deviceInfo)

        return ResponseEntity
            .ok()
            .body(LoginResponseDto(accessToken, newRefreshToken))
    }

    private fun loginByPhone(loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val user =
            userRepository.findByPhone(loginRequestDto.login)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_PHONE)
        return loginByEmail(loginRequestDto, user.email)
    }

    private fun loginByNickname(loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val user =
            userRepository.findByNickname(loginRequestDto.login)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_NICKNAME)
        return loginByEmail(loginRequestDto, user.email)
    }
}
