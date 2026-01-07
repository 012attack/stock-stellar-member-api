package yi.memberapi.application.required

import org.springframework.data.jpa.repository.JpaRepository
import yi.memberapi.domain.member.Member
import java.util.Optional

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(username: String): Optional<Member>
    fun existsByUsername(username: String): Boolean
}
