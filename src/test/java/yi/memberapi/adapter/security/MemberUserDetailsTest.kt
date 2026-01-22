package yi.memberapi.adapter.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import yi.memberapi.domain.member.Member

class MemberUserDetailsTest {

    @Test
    @DisplayName("Member로부터 UserDetails를 생성한다")
    fun createFromMember() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = "encoded_password",
            name = "Test User"
        )

        val userDetails = MemberUserDetails(member)

        assertEquals("testuser", userDetails.username)
        assertEquals("encoded_password", userDetails.password)
    }

    @Test
    @DisplayName("ROLE_USER 권한을 가진다")
    fun hasRoleUser() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = "password",
            name = "Test User"
        )

        val userDetails = MemberUserDetails(member)

        val authorities = userDetails.authorities
        assertEquals(1, authorities.size)
        assertTrue(authorities.contains(SimpleGrantedAuthority("ROLE_USER")))
    }

    @Test
    @DisplayName("계정 상태 메서드들은 true를 반환한다")
    fun accountStatusMethods() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = "password",
            name = "Test User"
        )

        val userDetails = MemberUserDetails(member)

        assertTrue(userDetails.isAccountNonExpired)
        assertTrue(userDetails.isAccountNonLocked)
        assertTrue(userDetails.isCredentialsNonExpired)
        assertTrue(userDetails.isEnabled)
    }

    @Test
    @DisplayName("getMember()로 원본 Member를 반환한다")
    fun getMember() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = "password",
            name = "Test User"
        )

        val userDetails = MemberUserDetails(member)

        assertEquals(member, userDetails.getMember())
        assertEquals(1L, userDetails.getMember().id)
    }

    @Test
    @DisplayName("username이 null인 경우 빈 문자열을 반환한다")
    fun nullUsername() {
        val member = Member(
            id = 1L,
            username = null,
            password = "password",
            name = "Test User"
        )

        val userDetails = MemberUserDetails(member)

        assertEquals("", userDetails.username)
    }

    @Test
    @DisplayName("password가 null인 경우 빈 문자열을 반환한다")
    fun nullPassword() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = null,
            name = "Test User"
        )

        val userDetails = MemberUserDetails(member)

        assertEquals("", userDetails.password)
    }
}
