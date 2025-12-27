package ru.shorin.authenticationservice.mapper

import org.springframework.stereotype.Component
import ru.shorin.authenticationservice.domain.DeviceInfo
import ru.shorin.authenticationservice.dto.common.ClientDto
import ru.shorin.authenticationservice.dto.common.DeviceInfoDto
import ru.shorin.authenticationservice.dto.common.OsDto
import ru.shorin.authenticationservice.dto.session.GetSessionsResponseDto
import ru.shorin.authenticationservice.dto.session.SessionDto
import ru.shorin.authenticationservice.model.Client
import ru.shorin.authenticationservice.model.Os
import ru.shorin.authenticationservice.model.RefreshToken
import ru.shorin.authenticationservice.model.User
import java.sql.Timestamp
import java.time.Instant

@Component
class SessionMapper {
    fun toRefreshToken(
        token: String,
        user: User,
        deviceInfo: DeviceInfo,
        expiresAt: Timestamp,
    ) = RefreshToken(
        token = token,
        user = user,
        deviceId = deviceInfo.deviceId,
        client = deviceInfo.client,
        os = deviceInfo.os,
        deviceName = deviceInfo.deviceName,
        country = deviceInfo.country,
        city = deviceInfo.city,
        expiresAt = expiresAt,
        createdAt = Timestamp.from(Instant.now()),
    )

    fun toGetSessionResponseDto(refreshTokens: List<RefreshToken>): GetSessionsResponseDto =
        GetSessionsResponseDto(refreshTokens.map { toSessionDto(it) })

    private fun toSessionDto(refreshToken: RefreshToken) =
        SessionDto(
            deviceId = refreshToken.deviceId,
            client = toClientDto(refreshToken.client),
            os = toOsDto(refreshToken.os),
            deviceName = refreshToken.deviceName,
            country = refreshToken.country,
            city = refreshToken.city,
            createdAt = refreshToken.createdAt.toInstant(),
        )

    fun toDeviceInfo(
        deviceInfo: DeviceInfoDto,
        country: String,
        city: String,
    ): DeviceInfo =
        DeviceInfo(
            deviceId = deviceInfo.deviceId,
            client = toClient(deviceInfo.client),
            os = toOs(deviceInfo.os),
            deviceName = deviceInfo.deviceName,
            country = country,
            city = city,
        )

    private fun toClientDto(client: Client): ClientDto =
        when (client) {
            Client.SAFARI -> ClientDto.SAFARI
            Client.CHROME -> ClientDto.CHROME
            Client.EDGE -> ClientDto.EDGE
            Client.MOZILLA -> ClientDto.MOZILLA
            Client.OPERA -> ClientDto.OPERA
            Client.YANDEX_BROWSER -> ClientDto.YANDEX_BROWSER
            Client.IOS_APP -> ClientDto.IOS_APP
            Client.ANDROID_APP -> ClientDto.ANDROID_APP
        }

    private fun toOsDto(os: Os): OsDto =
        when (os) {
            Os.IOS -> OsDto.IOS
            Os.ANDROID -> OsDto.ANDROID
            Os.MACOS -> OsDto.MACOS
            Os.WINDOWS -> OsDto.WINDOWS
            Os.LINUX -> OsDto.LINUX
        }

    private fun toClient(clientDto: ClientDto): Client =
        when (clientDto) {
            ClientDto.SAFARI -> Client.SAFARI
            ClientDto.CHROME -> Client.CHROME
            ClientDto.EDGE -> Client.EDGE
            ClientDto.MOZILLA -> Client.MOZILLA
            ClientDto.OPERA -> Client.OPERA
            ClientDto.YANDEX_BROWSER -> Client.YANDEX_BROWSER
            ClientDto.IOS_APP -> Client.IOS_APP
            ClientDto.ANDROID_APP -> Client.ANDROID_APP
        }

    private fun toOs(osDto: OsDto): Os =
        when (osDto) {
            OsDto.IOS -> Os.IOS
            OsDto.ANDROID -> Os.ANDROID
            OsDto.MACOS -> Os.MACOS
            OsDto.WINDOWS -> Os.WINDOWS
            OsDto.LINUX -> Os.LINUX
        }
}
