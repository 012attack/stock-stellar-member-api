package yi.memberapi.adapter.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import yi.memberapi.application.required.MemberRepository

@Service
class MemberUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }

        return MemberUserDetails(member)
    }
}
