package yi.memberapi.application.provided

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import yi.memberapi.domain.token.RefreshTokenInfo
import java.security.MessageDigest
import java.time.Duration
import java.util.*

@Repository
class RedisTokenRepository(
    private val refreshTokenRedisTemplate: RedisTemplate<String, RefreshTokenInfo>,
    private val tokenStringRedisTemplate: RedisTemplate<String, String>
) {
    companion object {
        private const val REFRESH_TOKEN_PREFIX = "refresh_token:"
        private const val ACCESS_TOKEN_PREFIX = "access_token:"
        private const val USER_TOKEN_PREFIX = "user_tokens:"
    }

    fun saveRefreshToken(token: String, tokenInfo: RefreshTokenInfo, ttlSeconds: Long) {
        val tokenHash = hashToken(token)
        val key = "$REFRESH_TOKEN_PREFIX$tokenHash"

        refreshTokenRedisTemplate.opsForValue().set(key, tokenInfo, Duration.ofSeconds(ttlSeconds))

        val userKey = "$USER_TOKEN_PREFIX${tokenInfo.memberId}"
        tokenStringRedisTemplate.opsForSet().add(userKey, tokenHash)
        tokenStringRedisTemplate.expire(userKey, Duration.ofSeconds(ttlSeconds))
    }

    fun findRefreshToken(token: String): RefreshTokenInfo? {
        val tokenHash = hashToken(token)
        val key = "$REFRESH_TOKEN_PREFIX$tokenHash"
        return refreshTokenRedisTemplate.opsForValue().get(key)
    }

    fun deleteRefreshToken(token: String) {
        val tokenHash = hashToken(token)
        val key = "$REFRESH_TOKEN_PREFIX$tokenHash"
        val tokenInfo = refreshTokenRedisTemplate.opsForValue().get(key)

        refreshTokenRedisTemplate.delete(key)

        tokenInfo?.let {
            val userKey = "$USER_TOKEN_PREFIX${it.memberId}"
            tokenStringRedisTemplate.opsForSet().remove(userKey, tokenHash)
        }
    }

    fun deleteAllUserTokens(memberId: Long) {
        val userKey = "$USER_TOKEN_PREFIX$memberId"
        val tokenHashes = tokenStringRedisTemplate.opsForSet().members(userKey) ?: emptySet()

        tokenHashes.forEach { hash ->
            refreshTokenRedisTemplate.delete("$REFRESH_TOKEN_PREFIX$hash")
        }
        tokenStringRedisTemplate.delete(userKey)
    }

    fun saveAccessToken(token: String, memberId: Long, ttlSeconds: Long) {
        val key = "$ACCESS_TOKEN_PREFIX${hashToken(token)}"
        tokenStringRedisTemplate.opsForValue().set(key, memberId.toString(), Duration.ofSeconds(ttlSeconds))
    }

    fun isAccessTokenValid(token: String): Boolean {
        val key = "$ACCESS_TOKEN_PREFIX${hashToken(token)}"
        return tokenStringRedisTemplate.hasKey(key)
    }

    fun invalidateAccessToken(token: String) {
        val key = "$ACCESS_TOKEN_PREFIX${hashToken(token)}"
        tokenStringRedisTemplate.delete(key)
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes)
    }
}
