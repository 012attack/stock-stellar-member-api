package yi.memberapi.application.required

import yi.memberapi.domain.member.Member
import java.util.Optional

interface MemberFinder {
    fun findById(id: Long): Optional<Member>
    fun findByUsername(username: String): Optional<Member>
    fun existsByUsername(username: String): Boolean
}
