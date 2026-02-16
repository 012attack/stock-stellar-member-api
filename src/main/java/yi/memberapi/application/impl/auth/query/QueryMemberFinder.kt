package yi.memberapi.application.impl.auth.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.MemberRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.domain.member.Member
import java.util.Optional

@Service
@Transactional(readOnly = true)
class QueryMemberFinder(
    private val memberRepository: MemberRepository
) : MemberFinder {

    override fun findById(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Member not found: $id") }
    }

    override fun findByUsername(username: String): Optional<Member> {
        return memberRepository.findByUsername(username)
    }

    override fun existsByUsername(username: String): Boolean {
        return memberRepository.existsByUsername(username)
    }
}
