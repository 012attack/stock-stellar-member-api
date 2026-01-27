package yi.memberapi.common.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class GlobalExceptionHandlerTest {

    private lateinit var exceptionHandler: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        exceptionHandler = GlobalExceptionHandler()
    }

    @Test
    @DisplayName("InvalidCredentialsException 처리 시 401을 반환한다")
    fun handleInvalidCredentials() {
        val exception = AuthException.InvalidCredentialsException()

        val problemDetail = exceptionHandler.handleAuthException(exception)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.status)
        assertEquals("Unauthorized", problemDetail.title)
        assertEquals("Invalid username or password", problemDetail.detail)
        assertNotNull(problemDetail.type)
    }

    @Test
    @DisplayName("InvalidTokenException 처리 시 401을 반환한다")
    fun handleInvalidToken() {
        val exception = AuthException.InvalidTokenException()

        val problemDetail = exceptionHandler.handleAuthException(exception)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.status)
        assertEquals("Unauthorized", problemDetail.title)
        assertEquals("Invalid or expired token", problemDetail.detail)
    }

    @Test
    @DisplayName("TokenExpiredException 처리 시 401을 반환한다")
    fun handleTokenExpired() {
        val exception = AuthException.TokenExpiredException()

        val problemDetail = exceptionHandler.handleAuthException(exception)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.status)
        assertEquals("Unauthorized", problemDetail.title)
        assertEquals("Token has expired", problemDetail.detail)
    }

    @Test
    @DisplayName("RefreshTokenNotFoundException 처리 시 401을 반환한다")
    fun handleRefreshTokenNotFound() {
        val exception = AuthException.RefreshTokenNotFoundException()

        val problemDetail = exceptionHandler.handleAuthException(exception)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.status)
        assertEquals("Unauthorized", problemDetail.title)
        assertEquals("Refresh token not found", problemDetail.detail)
    }

    @Test
    @DisplayName("UsernameAlreadyExistsException 처리 시 409를 반환한다")
    fun handleUsernameAlreadyExists() {
        val exception = AuthException.UsernameAlreadyExistsException()

        val problemDetail = exceptionHandler.handleAuthException(exception)

        assertEquals(HttpStatus.CONFLICT.value(), problemDetail.status)
        assertEquals("Conflict", problemDetail.title)
        assertEquals("Username already exists", problemDetail.detail)
    }

    @Test
    @DisplayName("IpMismatchException 처리 시 403을 반환한다")
    fun handleIpMismatch() {
        val exception = AuthException.IpMismatchException()

        val problemDetail = exceptionHandler.handleAuthException(exception)

        assertEquals(HttpStatus.FORBIDDEN.value(), problemDetail.status)
        assertEquals("Forbidden", problemDetail.title)
        assertEquals("Client IP mismatch detected. Please login again.", problemDetail.detail)
    }
}
