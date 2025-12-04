package ru.shorin.exception

import org.springframework.http.HttpStatus

data class BusinessException(
    val status: HttpStatus,
    override val message: String,
) : RuntimeException(message) {
    constructor(businessExceptionEnum: BusinessExceptionEnum) : this(
        businessExceptionEnum.status,
        businessExceptionEnum.message,
    )
}
