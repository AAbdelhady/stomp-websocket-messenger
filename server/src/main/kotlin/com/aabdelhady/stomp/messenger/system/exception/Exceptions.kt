package com.aabdelhady.stomp.messenger.system.exception

class Exceptions {
}
open class CustomException(val code: ErrorCode = ErrorCode.UNSPECIFIED) : RuntimeException()

class NotFoundException(override val message: String? = null) : CustomException()
class BadRequestException(override val message: String? = null) : CustomException()
class UnauthorizedException : CustomException()
class ForbiddenException : CustomException()

enum class ErrorCode {
    SERVER_ERROR,
    UNSPECIFIED,
    VALIDATION_ERROR
}
