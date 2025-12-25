package ru.shorin.authenticationservice.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.shorin.dto.BusinessExceptionResponseDto
import ru.shorin.dto.InternalErrorResponseDto
import ru.shorin.dto.ValidationExceptionResponseDto
import ru.shorin.exception.BusinessException
import ru.shorin.exception.BusinessExceptionEnum

@RestControllerAdvice
class BusinessExceptionsHandler : ResponseEntityExceptionHandler() {
    private val log = LoggerFactory.getLogger(BusinessExceptionsHandler::class.java)

    override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val validationExceptions =
            exception.bindingResult.fieldErrors.map {
                ValidationExceptionResponseDto(
                    field = it.field,
                    message = it.defaultMessage,
                )
            }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(validationExceptions)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRuntimeException(runtimeException: RuntimeException): ResponseEntity<InternalErrorResponseDto> {
        log.error("Unhandled runtime exception: ", runtimeException)

        return ResponseEntity
            .status(BusinessExceptionEnum.INTERNAL_SERVER_ERROR.status)
            .body(
                InternalErrorResponseDto(
                    userMessage = BusinessExceptionEnum.INTERNAL_SERVER_ERROR.message,
                    errorMessage = runtimeException.message,
                ),
            )
    }

    @ExceptionHandler(value = [BusinessException::class])
    fun handleBusinessException(businessException: BusinessException): ResponseEntity<BusinessExceptionResponseDto> =
        ResponseEntity
            .status(businessException.status)
            .body(
                BusinessExceptionResponseDto(
                    message = businessException.message,
                ),
            )

    @ExceptionHandler(value = [BadCredentialsException::class])
    fun handleBadCredentialsException(): ResponseEntity<BusinessExceptionResponseDto> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                BusinessExceptionResponseDto(
                    message = "Неверный пароль",
                ),
            )

    @ExceptionHandler(value = [InternalAuthenticationServiceException::class])
    fun handleInternalAuthenticationServiceException(): ResponseEntity<BusinessExceptionResponseDto> =
        ResponseEntity
            .status(BusinessExceptionEnum.USER_NOT_FOUND_BY_EMAIL.status)
            .body(
                BusinessExceptionResponseDto(
                    message = BusinessExceptionEnum.USER_NOT_FOUND_BY_EMAIL.message,
                ),
            )
}
