package yi.memberapi.common.exception

import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthException::class)
    fun handleAuthException(ex: AuthException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(ex.status, ex.message ?: "Authentication error").apply {
            type = URI.create("https://api.member.yi/errors/${ex.javaClass.simpleName.lowercase()}")
            title = ex.status.reasonPhrase
        }
    }
}
