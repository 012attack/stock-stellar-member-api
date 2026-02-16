package yi.memberapi.application.impl.auth.command

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.request.RegisterRequest
import yi.memberapi.adapter.webapi.dto.response.RegisterResponse
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.application.required.MemberRegister
import yi.memberapi.application.provided.MemberRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.domain.member.Member

@Service
@Transactional
class CommandMemberRegister(
    private val memberRepository: MemberRepository,
    private val memberFinder: MemberFinder,
    private val passwordEncoder: PasswordEncoder
) : MemberRegister {

    override fun register(request: RegisterRequest): RegisterResponse {
        if (memberFinder.existsByUsername(request.username)) {
            throw AuthException.UsernameAlreadyExistsException()
        }

        val member = Member(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )
        val savedMember = memberRepository.save(member)

        return RegisterResponse(
            id = savedMember.id!!,
            username = savedMember.username!!,
            name = savedMember.name!!
        )
    }
}
