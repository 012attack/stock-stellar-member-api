package yi.memberapi.application.required.auth

import yi.memberapi.domain.member.Member
import java.util.Optional

interface MemberFinder {
    fun findById(id: Long): Member
    fun findByUsername(username: String): Optional<Member>
    fun existsByUsername(username: String): Boolean
}
