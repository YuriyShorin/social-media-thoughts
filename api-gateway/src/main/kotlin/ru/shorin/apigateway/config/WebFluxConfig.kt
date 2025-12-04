package ru.shorin.apigateway.config

import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.CodecConfigurer
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class WebFluxConfig {
    @Bean
    @LoadBalanced
    fun webClient(): WebClient.Builder {
        val httpClient: HttpClient =
            HttpClient
                .create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
        return WebClient
            .builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
    }

    @Bean
    fun resources(): WebProperties.Resources = WebProperties.Resources()

    @Bean
    fun codecConfigurer(): CodecConfigurer = DefaultServerCodecConfigurer()
}
