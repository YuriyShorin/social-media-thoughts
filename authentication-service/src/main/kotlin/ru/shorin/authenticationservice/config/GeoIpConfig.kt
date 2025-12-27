package ru.shorin.authenticationservice.config

import com.maxmind.geoip2.DatabaseReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class GeoIpConfig(
    private val resourceLoader: ResourceLoader,
) {
    @Bean
    fun geoIpDatabaseReader(): DatabaseReader {
        val inputStream =
            resourceLoader
                .getResource("classpath:geoip/geoip.mmdb")
                .inputStream

        return DatabaseReader.Builder(inputStream).build()
    }
}
