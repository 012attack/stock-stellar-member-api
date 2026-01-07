package yi.memberapi.application.required

import org.springframework.data.jpa.repository.JpaRepository
import yi.memberapi.domain.token.RefreshToken
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>
    fun findByUsername(username: String): Optional<RefreshToken>
    fun deleteByUsername(username: String)
}
