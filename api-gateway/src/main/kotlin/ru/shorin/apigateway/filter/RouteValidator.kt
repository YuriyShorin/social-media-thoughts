package ru.shorin.apigateway.filter

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
class RouteValidator {
    companion object {
        private val OPEN_API_ENDPOINTS =
            listOf(
                "/api/v1/authentication/signup",
                "/api/v1/authentication/login",
                "/api/v1/authentication/refresh",
                "/v3/api-docs",
                "/eureka",
            )
    }

    fun isSecured(): Predicate<ServerHttpRequest> =
        Predicate { request ->
            OPEN_API_ENDPOINTS.none { uri -> request.uri.path.contains(uri) }
        }
}
