package ru.shorin.apigateway.handler

import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.webflux.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.codec.CodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes,
    resources: WebProperties.Resources,
    applicationContext: ApplicationContext,
    codecConfigurer: CodecConfigurer,
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {
    init {
        this.setMessageReaders(codecConfigurer.readers)
        this.setMessageWriters(codecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> =
        RouterFunctions.route(RequestPredicates.all(), this::renderException)

    private fun renderException(request: ServerRequest): Mono<ServerResponse> {
        val error = this.getErrorAttributes(request, ErrorAttributeOptions.defaults()).toMutableMap()
        val status = error.remove("status") as? HttpStatusCode ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.remove("requestId")
        return ServerResponse
            .status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(error))
    }
}
