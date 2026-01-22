package yi.memberapi.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.InvalidCredentialsException::class)
    fun handleInvalidCredentials(ex: AuthException.InvalidCredentialsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = "Unauthorized",
                message = ex.message ?: "Invalid credentials"
            ))
    }

    @ExceptionHandler(AuthException.InvalidTokenException::class)
    fun handleInvalidToken(ex: AuthException.InvalidTokenException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = "Unauthorized",
                message = ex.message ?: "Invalid token"
            ))
    }

    @ExceptionHandler(AuthException.TokenExpiredException::class)
    fun handleTokenExpired(ex: AuthException.TokenExpiredException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = "Unauthorized",
                message = ex.message ?: "Token expired"
            ))
    }

    @ExceptionHandler(AuthException.RefreshTokenNotFoundException::class)
    fun handleRefreshTokenNotFound(ex: AuthException.RefreshTokenNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = "Unauthorized",
                message = ex.message ?: "Refresh token not found"
            ))
    }

    @ExceptionHandler(AuthException.UsernameAlreadyExistsException::class)
    fun handleUsernameAlreadyExists(ex: AuthException.UsernameAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                status = HttpStatus.CONFLICT.value(),
                error = "Conflict",
                message = ex.message ?: "Username already exists"
            ))
    }

    @ExceptionHandler(AuthException.IpMismatchException::class)
    fun handleIpMismatch(ex: AuthException.IpMismatchException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(
                status = HttpStatus.FORBIDDEN.value(),
                error = "Forbidden",
                message = ex.message ?: "IP mismatch"
            ))
    }
}

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val timestamp: Long = Instant.now().toEpochMilli()
)
