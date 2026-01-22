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

        val response = exceptionHandler.handleInvalidCredentials(exception)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals(401, response.body?.status)
        assertEquals("Unauthorized", response.body?.error)
        assertNotNull(response.body?.message)
        assertNotNull(response.body?.timestamp)
    }

    @Test
    @DisplayName("InvalidTokenException 처리 시 401을 반환한다")
    fun handleInvalidToken() {
        val exception = AuthException.InvalidTokenException()

        val response = exceptionHandler.handleInvalidToken(exception)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals(401, response.body?.status)
        assertEquals("Unauthorized", response.body?.error)
    }

    @Test
    @DisplayName("TokenExpiredException 처리 시 401을 반환한다")
    fun handleTokenExpired() {
        val exception = AuthException.TokenExpiredException()

        val response = exceptionHandler.handleTokenExpired(exception)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals(401, response.body?.status)
        assertEquals("Unauthorized", response.body?.error)
    }

    @Test
    @DisplayName("RefreshTokenNotFoundException 처리 시 401을 반환한다")
    fun handleRefreshTokenNotFound() {
        val exception = AuthException.RefreshTokenNotFoundException()

        val response = exceptionHandler.handleRefreshTokenNotFound(exception)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals(401, response.body?.status)
        assertEquals("Unauthorized", response.body?.error)
    }

    @Test
    @DisplayName("UsernameAlreadyExistsException 처리 시 409를 반환한다")
    fun handleUsernameAlreadyExists() {
        val exception = AuthException.UsernameAlreadyExistsException()

        val response = exceptionHandler.handleUsernameAlreadyExists(exception)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals(409, response.body?.status)
        assertEquals("Conflict", response.body?.error)
    }

    @Test
    @DisplayName("IpMismatchException 처리 시 403을 반환한다")
    fun handleIpMismatch() {
        val exception = AuthException.IpMismatchException()

        val response = exceptionHandler.handleIpMismatch(exception)

        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        assertEquals(403, response.body?.status)
        assertEquals("Forbidden", response.body?.error)
    }
}
