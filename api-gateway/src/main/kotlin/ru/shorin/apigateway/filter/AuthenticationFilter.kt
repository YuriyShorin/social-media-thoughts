package ru.shorin.apigateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import ru.shorin.exception.BusinessException
import ru.shorin.exception.BusinessExceptionEnum
import ru.shorin.utils.JwtUtils

@Component
class AuthenticationFilter(
    private val routeValidator: RouteValidator,
    private val jwtUtils: JwtUtils,
) : AbstractGatewayFilterFactory<AuthenticationFilter.Config>(Config::class.java) {
    private val log = LoggerFactory.getLogger(AuthenticationFilter::class.java)

    override fun apply(config: Config): GatewayFilter =
        GatewayFilter { exchange: ServerWebExchange, chain ->
            if (routeValidator.isSecured().test(exchange.request)) {
                val headers = exchange.request.headers

                val jwtToken =
                    headers[HttpHeaders.AUTHORIZATION]
                        ?.firstOrNull()
                        ?.takeIf { it.startsWith("Bearer ") }
                        ?.substring(7)
                        ?: throw BusinessException(BusinessExceptionEnum.MISSING_AUTH_HEADER)

                try {
                    jwtUtils.validateToken(jwtToken)
                } catch (_: Exception) {
                    throw BusinessException(BusinessExceptionEnum.UNAUTHORIZED_ACCESS)
                }

                if (jwtUtils.isTokenExpired(jwtToken)) {
                    throw BusinessException(BusinessExceptionEnum.TOKEN_EXPIRED)
                }
            }

            chain
                .filter(exchange)
                .doFinally { signalType ->
                    val path = exchange.request.path
                    val statusCode: HttpStatusCode? = exchange.response.statusCode
                    log.info("Запрос по пути {} завершился с сигналом {} и статусом {}", path, signalType, statusCode)
                }
        }

    class Config
}
