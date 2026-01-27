package yi.memberapi.application.provided

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import yi.memberapi.domain.token.RefreshTokenInfo
import java.security.MessageDigest
import java.time.Duration
import java.util.*

@Repository
class RedisTokenRepository(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    companion object {
        private const val REFRESH_TOKEN_PREFIX = "refresh_token:"
        private const val ACCESS_TOKEN_PREFIX = "access_token:"
        private const val USER_TOKEN_PREFIX = "user_tokens:"
    }

    fun saveRefreshToken(token: String, tokenInfo: RefreshTokenInfo, ttlSeconds: Long) {
        val tokenHash = hashToken(token)
        val key = "$REFRESH_TOKEN_PREFIX$tokenHash"

        redisTemplate.opsForValue().set(key, tokenInfo, Duration.ofSeconds(ttlSeconds))

        val userKey = "$USER_TOKEN_PREFIX${tokenInfo.memberId}"
        redisTemplate.opsForSet().add(userKey, tokenHash)
        redisTemplate.expire(userKey, Duration.ofSeconds(ttlSeconds))
    }

    fun findRefreshToken(token: String): RefreshTokenInfo? {
        val tokenHash = hashToken(token)
        val key = "$REFRESH_TOKEN_PREFIX$tokenHash"
        return redisTemplate.opsForValue().get(key) as? RefreshTokenInfo
    }

    fun deleteRefreshToken(token: String) {
        val tokenHash = hashToken(token)
        val key = "$REFRESH_TOKEN_PREFIX$tokenHash"
        val tokenInfo = redisTemplate.opsForValue().get(key) as? RefreshTokenInfo

        redisTemplate.delete(key)

        tokenInfo?.let {
            val userKey = "$USER_TOKEN_PREFIX${it.memberId}"
            redisTemplate.opsForSet().remove(userKey, tokenHash)
        }
    }

    fun deleteAllUserTokens(memberId: Long) {
        val userKey = "$USER_TOKEN_PREFIX$memberId"
        val tokenHashes = redisTemplate.opsForSet().members(userKey) ?: emptySet()

        tokenHashes.forEach { hash ->
            redisTemplate.delete("$REFRESH_TOKEN_PREFIX$hash")
        }
        redisTemplate.delete(userKey)
    }

    fun saveAccessToken(token: String, memberId: Long, ttlSeconds: Long) {
        val key = "$ACCESS_TOKEN_PREFIX${hashToken(token)}"
        redisTemplate.opsForValue().set(key, memberId, Duration.ofSeconds(ttlSeconds))
    }

    fun isAccessTokenValid(token: String): Boolean {
        val key = "$ACCESS_TOKEN_PREFIX${hashToken(token)}"
        return redisTemplate.hasKey(key)
    }

    fun invalidateAccessToken(token: String) {
        val key = "$ACCESS_TOKEN_PREFIX${hashToken(token)}"
        redisTemplate.delete(key)
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes)
    }
}
