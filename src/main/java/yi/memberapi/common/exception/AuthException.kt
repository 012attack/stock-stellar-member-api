package yi.memberapi.common.exception

sealed class AuthException(message: String) : RuntimeException(message) {
    class InvalidCredentialsException : AuthException("Invalid username or password")
    class InvalidTokenException : AuthException("Invalid or expired token")
    class TokenExpiredException : AuthException("Token has expired")
    class RefreshTokenNotFoundException : AuthException("Refresh token not found")
    class UsernameAlreadyExistsException : AuthException("Username already exists")
}
