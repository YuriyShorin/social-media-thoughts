package ru.shorin.authenticationservice.service

import com.maxmind.geoip2.DatabaseReader
import org.springframework.stereotype.Service
import java.net.InetAddress

@Service
class GeoIpService(
    private val databaseReader: DatabaseReader,
) {
    fun resolve(ip: String): Pair<String, String> {
        val address = InetAddress.getByName(ip)
        val response = databaseReader.city(address)

        val country =
            response
                .country()
                ?.names()
                ?.get("ru")
                ?: ""

        val city =
            response
                .city()
                ?.names()
                ?.get("ru")
                ?: ""

        return country to city
    }
}
