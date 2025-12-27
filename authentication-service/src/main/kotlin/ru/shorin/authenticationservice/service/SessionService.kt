package ru.shorin.authenticationservice.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.shorin.authenticationservice.dto.logout.LogoutRequestDto
import ru.shorin.authenticationservice.dto.session.GetSessionsResponseDto
import ru.shorin.authenticationservice.mapper.SessionMapper
import ru.shorin.authenticationservice.model.User
import ru.shorin.exception.BusinessException
import ru.shorin.exception.BusinessExceptionEnum

@Service
class SessionService(
    private val refreshTokenService: RefreshTokenService,
    private val geoIpService: GeoIpService,
    private val sessionMapper: SessionMapper,
) {
    @Transactional(readOnly = true)
    fun getSessions(user: User): ResponseEntity<GetSessionsResponseDto> {
        val refreshTokens = refreshTokenService.findByUserId(user.id)
        val response = sessionMapper.toGetSessionResponseDto(refreshTokens)
        return ResponseEntity.ok(response)
    }

    @Transactional
    fun logout(
        logoutRequestDto: LogoutRequestDto,
        user: User,
    ): ResponseEntity<Void> {
        val (county, city) = geoIpService.resolve(logoutRequestDto.deviceInfo.ip)
        val deviceInfo = sessionMapper.toDeviceInfo(logoutRequestDto.deviceInfo, county, city)
        val refreshToken = refreshTokenService.findByDeviceInfo(deviceInfo)

        refreshToken?.let {
            if (refreshToken.user.id != user.id) {
                throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)
            }
            refreshTokenService.revokeByToken(refreshToken.token)
        }

        return ResponseEntity
            .ok()
            .build()
    }

    @Transactional
    fun logoutAll(user: User): ResponseEntity<Void> {
        refreshTokenService.revokeByUserId(user.id)
        return ResponseEntity
            .ok()
            .build()
    }
}
