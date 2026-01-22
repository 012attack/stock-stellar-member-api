package yi.memberapi.domain.token

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

class RefreshTokenInfoTest {

    @Nested
    @DisplayName("만료 여부 테스트")
    inner class ExpirationTest {

        @Test
        @DisplayName("만료되지 않은 토큰은 false를 반환한다")
        fun isExpired_notExpired() {
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = "127.0.0.1",
                rememberMe = false,
                expiresAt = Instant.now().plusSeconds(3600).toEpochMilli()
            )

            assertFalse(tokenInfo.isExpired())
        }

        @Test
        @DisplayName("만료된 토큰은 true를 반환한다")
        fun isExpired_expired() {
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = "127.0.0.1",
                rememberMe = false,
                expiresAt = Instant.now().minusSeconds(3600).toEpochMilli()
            )

            assertTrue(tokenInfo.isExpired())
        }
    }

    @Nested
    @DisplayName("남은 시간 계산 테스트")
    inner class RemainingSecondsTest {

        @Test
        @DisplayName("남은 시간을 초 단위로 반환한다")
        fun getRemainingSeconds_positive() {
            val expiresIn = 3600L
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = "127.0.0.1",
                rememberMe = false,
                expiresAt = Instant.now().plusSeconds(expiresIn).toEpochMilli()
            )

            val remainingSeconds = tokenInfo.getRemainingSeconds()

            assertTrue(remainingSeconds > 0)
            assertTrue(remainingSeconds <= expiresIn)
        }

        @Test
        @DisplayName("만료된 토큰은 0을 반환한다")
        fun getRemainingSeconds_expired() {
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = "127.0.0.1",
                rememberMe = false,
                expiresAt = Instant.now().minusSeconds(3600).toEpochMilli()
            )

            assertEquals(0L, tokenInfo.getRemainingSeconds())
        }
    }

    @Nested
    @DisplayName("데이터 클래스 테스트")
    inner class DataClassTest {

        @Test
        @DisplayName("모든 필드가 올바르게 설정된다")
        fun allFieldsSet() {
            val now = Instant.now().toEpochMilli()
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash123",
                clientIp = "192.168.1.1",
                rememberMe = true,
                expiresAt = now + 3600000,
                createdAt = now
            )

            assertEquals(1L, tokenInfo.memberId)
            assertEquals("testuser", tokenInfo.username)
            assertEquals("hash123", tokenInfo.tokenHash)
            assertEquals("192.168.1.1", tokenInfo.clientIp)
            assertTrue(tokenInfo.rememberMe)
            assertEquals(now + 3600000, tokenInfo.expiresAt)
            assertEquals(now, tokenInfo.createdAt)
        }

        @Test
        @DisplayName("기본 createdAt은 현재 시간으로 설정된다")
        fun defaultCreatedAt() {
            val before = Instant.now().toEpochMilli()
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = "127.0.0.1",
                rememberMe = false,
                expiresAt = Instant.now().plusSeconds(3600).toEpochMilli()
            )
            val after = Instant.now().toEpochMilli()

            assertTrue(tokenInfo.createdAt >= before)
            assertTrue(tokenInfo.createdAt <= after)
        }
    }
}
