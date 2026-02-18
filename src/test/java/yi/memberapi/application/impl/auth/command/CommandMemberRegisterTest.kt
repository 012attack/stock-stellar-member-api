package yi.memberapi.application.impl.auth.command

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import yi.memberapi.adapter.webapi.auth.dto.request.RegisterRequest
import yi.memberapi.application.provided.MemberRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.common.exception.AuthException
import yi.memberapi.domain.member.Member

class CommandMemberRegisterTest {

    private lateinit var memberRepository: MemberRepository
    private lateinit var memberFinder: MemberFinder
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var commandMemberRegister: CommandMemberRegister

    @BeforeEach
    fun setUp() {
        memberRepository = mockk(relaxed = true)
        memberFinder = mockk(relaxed = true)
        passwordEncoder = mockk(relaxed = true)

        commandMemberRegister = CommandMemberRegister(
            memberRepository,
            memberFinder,
            passwordEncoder
        )
    }

    @Nested
    @DisplayName("회원가입 테스트")
    inner class RegisterTest {

        @Test
        @DisplayName("회원가입을 성공적으로 처리한다")
        fun register_success() {
            val request = RegisterRequest(
                username = "newuser",
                password = "password123",
                name = "New User"
            )
            val savedMember = Member(
                id = 1L,
                username = "newuser",
                password = "encoded_password",
                name = "New User"
            )

            every { memberFinder.existsByUsername(request.username) } returns false
            every { passwordEncoder.encode(request.password) } returns "encoded_password"
            every { memberRepository.save(any()) } returns savedMember

            val result = commandMemberRegister.register(request)

            assertEquals(1L, result.id)
            assertEquals("newuser", result.username)
            assertEquals("New User", result.name)

            verify { memberFinder.existsByUsername(request.username) }
            verify { memberRepository.save(any()) }
        }

        @Test
        @DisplayName("이미 존재하는 username으로 회원가입 시 예외가 발생한다")
        fun register_usernameAlreadyExists() {
            val request = RegisterRequest(
                username = "existinguser",
                password = "password123",
                name = "Existing User"
            )

            every { memberFinder.existsByUsername(request.username) } returns true

            assertThrows<AuthException.UsernameAlreadyExistsException> {
                commandMemberRegister.register(request)
            }

            verify { memberFinder.existsByUsername(request.username) }
            verify(exactly = 0) { memberRepository.save(any()) }
        }
    }
}
