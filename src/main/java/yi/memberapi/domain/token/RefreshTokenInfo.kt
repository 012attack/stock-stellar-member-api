package yi.memberapi.domain.token

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.time.Instant

data class RefreshTokenInfo(
    val memberId: Long,
    val username: String,
    val tokenHash: String,
    val clientIp: String,
    val rememberMe: Boolean,
    val expiresAt: Long,
    val createdAt: Long = Instant.now().toEpochMilli()
) : Serializable {

    @JsonIgnore
    fun isExpired(): Boolean = Instant.now().toEpochMilli() > expiresAt

    @JsonIgnore
    fun getRemainingSeconds(): Long {
        val remaining = (expiresAt - Instant.now().toEpochMilli()) / 1000
        return if (remaining > 0) remaining else 0
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
