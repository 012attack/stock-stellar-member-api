package yi.memberapi.common.exception

import org.springframework.http.HttpStatus

sealed class AuthException(
    message: String,
    val status: HttpStatus
) : RuntimeException(message) {

    class InvalidCredentialsException : AuthException(
        message = "Invalid username or password",
        status = HttpStatus.UNAUTHORIZED
    )

    class InvalidTokenException : AuthException(
        message = "Invalid or expired token",
        status = HttpStatus.UNAUTHORIZED
    )

    class TokenExpiredException : AuthException(
        message = "Token has expired",
        status = HttpStatus.UNAUTHORIZED
    )

    class RefreshTokenNotFoundException : AuthException(
        message = "Refresh token not found",
        status = HttpStatus.UNAUTHORIZED
    )

    class UsernameAlreadyExistsException : AuthException(
        message = "Username already exists",
        status = HttpStatus.CONFLICT
    )

    class IpMismatchException : AuthException(
        message = "Client IP mismatch detected. Please login again.",
        status = HttpStatus.FORBIDDEN
    )
}
