package yi.memberapi.application.provided

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.ValueOperations

import yi.memberapi.domain.token.RefreshTokenInfo
import java.time.Duration
import java.time.Instant

class RedisTokenRepositoryTest {

    private lateinit var refreshTokenRedisTemplate: RedisTemplate<String, RefreshTokenInfo>
    private lateinit var tokenStringRedisTemplate: RedisTemplate<String, String>
    private lateinit var refreshValueOperations: ValueOperations<String, RefreshTokenInfo>
    private lateinit var stringValueOperations: ValueOperations<String, String>
    private lateinit var stringSetOperations: SetOperations<String, String>
    private lateinit var redisTokenRepository: RedisTokenRepository

    @BeforeEach
    fun setUp() {
        refreshTokenRedisTemplate = mockk(relaxed = true)
        tokenStringRedisTemplate = mockk(relaxed = true)
        refreshValueOperations = mockk(relaxed = true)
        stringValueOperations = mockk(relaxed = true)
        stringSetOperations = mockk(relaxed = true)

        every { refreshTokenRedisTemplate.opsForValue() } returns refreshValueOperations
        every { tokenStringRedisTemplate.opsForValue() } returns stringValueOperations
        every { tokenStringRedisTemplate.opsForSet() } returns stringSetOperations

        redisTokenRepository = RedisTokenRepository(refreshTokenRedisTemplate, tokenStringRedisTemplate)
    }

    @Nested
    @DisplayName("Refresh Token 저장 테스트")
    inner class SaveRefreshTokenTest {

        @Test
        @DisplayName("Refresh Token을 성공적으로 저장한다")
        fun saveRefreshToken_success() {
            val token = "test-refresh-token"
            val tokenInfo = createRefreshTokenInfo()
            val ttlSeconds = 604800L

            redisTokenRepository.saveRefreshToken(token, tokenInfo, ttlSeconds)

            verify {
                refreshValueOperations.set(match { it.startsWith("refresh_token:") }, eq(tokenInfo), any<Duration>())
            }
            verify {
                stringSetOperations.add(eq("user_tokens:${tokenInfo.memberId}"), any<String>())
            }
        }
    }

    @Nested
    @DisplayName("Refresh Token 조회 테스트")
    inner class FindRefreshTokenTest {

        @Test
        @DisplayName("존재하는 Refresh Token을 조회한다")
        fun findRefreshToken_exists() {
            val token = "test-refresh-token"
            val tokenInfo = createRefreshTokenInfo()

            every { refreshValueOperations.get(any<String>()) } returns tokenInfo

            val result = redisTokenRepository.findRefreshToken(token)

            assertNotNull(result)
            assertEquals(tokenInfo.memberId, result?.memberId)
            assertEquals(tokenInfo.username, result?.username)
        }

        @Test
        @DisplayName("존재하지 않는 Refresh Token은 null을 반환한다")
        fun findRefreshToken_notExists() {
            val token = "non-existent-token"

            every { refreshValueOperations.get(any<String>()) } returns null

            val result = redisTokenRepository.findRefreshToken(token)

            assertNull(result)
        }
    }

    @Nested
    @DisplayName("Refresh Token 삭제 테스트")
    inner class DeleteRefreshTokenTest {

        @Test
        @DisplayName("Refresh Token을 삭제한다")
        fun deleteRefreshToken_success() {
            val token = "test-refresh-token"
            val tokenInfo = createRefreshTokenInfo()

            every { refreshValueOperations.get(any<String>()) } returns tokenInfo
            every { refreshTokenRedisTemplate.delete(any<String>()) } returns true

            redisTokenRepository.deleteRefreshToken(token)

            verify { refreshTokenRedisTemplate.delete(match<String> { it.startsWith("refresh_token:") }) }
            verify { stringSetOperations.remove(eq("user_tokens:${tokenInfo.memberId}"), any()) }
        }

        @Test
        @DisplayName("존재하지 않는 Token 삭제 시 예외가 발생하지 않는다")
        fun deleteRefreshToken_notExists() {
            val token = "non-existent-token"

            every { refreshValueOperations.get(any<String>()) } returns null
            every { refreshTokenRedisTemplate.delete(any<String>()) } returns false

            assertDoesNotThrow {
                redisTokenRepository.deleteRefreshToken(token)
            }
        }
    }

    @Nested
    @DisplayName("사용자 전체 Token 삭제 테스트")
    inner class DeleteAllUserTokensTest {

        @Test
        @DisplayName("사용자의 모든 Token을 삭제한다")
        fun deleteAllUserTokens_success() {
            val memberId = 1L
            val tokenHashes = setOf("hash1", "hash2", "hash3")

            every { stringSetOperations.members("user_tokens:$memberId") } returns tokenHashes
            every { refreshTokenRedisTemplate.delete(any<String>()) } returns true
            every { tokenStringRedisTemplate.delete(any<String>()) } returns true

            redisTokenRepository.deleteAllUserTokens(memberId)

            verify(exactly = 3) { refreshTokenRedisTemplate.delete(match<String> { it.startsWith("refresh_token:") }) }
            verify { tokenStringRedisTemplate.delete("user_tokens:$memberId") }
        }

        @Test
        @DisplayName("사용자의 Token이 없는 경우에도 예외가 발생하지 않는다")
        fun deleteAllUserTokens_noTokens() {
            val memberId = 1L

            every { stringSetOperations.members("user_tokens:$memberId") } returns emptySet()

            assertDoesNotThrow {
                redisTokenRepository.deleteAllUserTokens(memberId)
            }
        }
    }

    @Nested
    @DisplayName("Access Token 관리 테스트")
    inner class AccessTokenTest {

        @Test
        @DisplayName("Access Token을 저장한다")
        fun saveAccessToken_success() {
            val token = "test-access-token"
            val memberId = 1L
            val ttlSeconds = 1800L

            redisTokenRepository.saveAccessToken(token, memberId, ttlSeconds)

            verify {
                stringValueOperations.set(
                    match { it.startsWith("access_token:") },
                    eq(memberId.toString()),
                    any<Duration>()
                )
            }
        }

        @Test
        @DisplayName("유효한 Access Token은 true를 반환한다")
        fun isAccessTokenValid_valid() {
            val token = "valid-access-token"

            every { tokenStringRedisTemplate.hasKey(any<String>()) } returns true

            val result = redisTokenRepository.isAccessTokenValid(token)

            assertTrue(result)
        }

        @Test
        @DisplayName("유효하지 않은 Access Token은 false를 반환한다")
        fun isAccessTokenValid_invalid() {
            val token = "invalid-access-token"

            every { tokenStringRedisTemplate.hasKey(any<String>()) } returns false

            val result = redisTokenRepository.isAccessTokenValid(token)

            assertFalse(result)
        }

        @Test
        @DisplayName("Access Token을 무효화한다")
        fun invalidateAccessToken_success() {
            val token = "test-access-token"

            every { tokenStringRedisTemplate.delete(any<String>()) } returns true

            redisTokenRepository.invalidateAccessToken(token)

            verify { tokenStringRedisTemplate.delete(match<String> { it.startsWith("access_token:") }) }
        }
    }

    private fun createRefreshTokenInfo(
        memberId: Long = 1L,
        username: String = "testuser",
        clientIp: String = "127.0.0.1",
        rememberMe: Boolean = false
    ): RefreshTokenInfo {
        return RefreshTokenInfo(
            memberId = memberId,
            username = username,
            tokenHash = "test-token-hash",
            clientIp = clientIp,
            rememberMe = rememberMe,
            expiresAt = Instant.now().plusSeconds(604800).toEpochMilli()
        )
    }
}
