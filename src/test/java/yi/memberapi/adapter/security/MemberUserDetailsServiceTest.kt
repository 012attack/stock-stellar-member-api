package yi.memberapi.adapter.security

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException
import yi.memberapi.application.required.MemberRepository
import yi.memberapi.domain.member.Member
import java.util.*

class MemberUserDetailsServiceTest {

    private lateinit var memberRepository: MemberRepository
    private lateinit var memberUserDetailsService: MemberUserDetailsService

    @BeforeEach
    fun setUp() {
        memberRepository = mockk()
        memberUserDetailsService = MemberUserDetailsService(memberRepository)
    }

    @Test
    @DisplayName("존재하는 username으로 UserDetails를 로드한다")
    fun loadUserByUsername_success() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = "encoded_password",
            name = "Test User"
        )

        every { memberRepository.findByUsername("testuser") } returns Optional.of(member)

        val userDetails = memberUserDetailsService.loadUserByUsername("testuser")

        assertNotNull(userDetails)
        assertEquals("testuser", userDetails.username)
        assertEquals("encoded_password", userDetails.password)
        assertTrue(userDetails is MemberUserDetails)

        verify { memberRepository.findByUsername("testuser") }
    }

    @Test
    @DisplayName("존재하지 않는 username으로 로드 시 예외가 발생한다")
    fun loadUserByUsername_notFound() {
        every { memberRepository.findByUsername("nonexistent") } returns Optional.empty()

        assertThrows<UsernameNotFoundException> {
            memberUserDetailsService.loadUserByUsername("nonexistent")
        }

        verify { memberRepository.findByUsername("nonexistent") }
    }

    @Test
    @DisplayName("MemberUserDetails로 캐스팅하여 Member에 접근할 수 있다")
    fun loadUserByUsername_accessMember() {
        val member = Member(
            id = 1L,
            username = "testuser",
            password = "encoded_password",
            name = "Test User"
        )

        every { memberRepository.findByUsername("testuser") } returns Optional.of(member)

        val userDetails = memberUserDetailsService.loadUserByUsername("testuser") as MemberUserDetails

        assertEquals(member, userDetails.getMember())
        assertEquals(1L, userDetails.getMember().id)
        assertEquals("Test User", userDetails.getMember().name)
    }
}
