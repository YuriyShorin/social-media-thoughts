package ru.shorin.apigateway.handler

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import ru.shorin.exception.BusinessException

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(
        request: ServerRequest,
        options: ErrorAttributeOptions,
    ): Map<String, Any> {
        val error = getError(request)
        val statusCode = determineHttpStatus(error)

        return mapOf(
            "status" to statusCode,
            "message" to (error.message ?: "Unknown error"),
            "endpoint_url" to request.path(),
        )
    }

    private fun determineHttpStatus(throwable: Throwable): HttpStatusCode =
        if (throwable is BusinessException) {
            throwable.status
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
}
