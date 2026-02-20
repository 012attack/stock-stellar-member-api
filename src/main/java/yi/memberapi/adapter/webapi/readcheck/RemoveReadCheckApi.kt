package yi.memberapi.adapter.webapi.readcheck

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.ReadCheckRemover
import yi.memberapi.domain.readcheck.ReadCheckTargetType

@RestController
@RequestMapping("/api/read-checks")
class RemoveReadCheckApi(
    private val readCheckRemover: ReadCheckRemover
) {

    @DeleteMapping
    fun removeReadCheck(
        @RequestParam targetType: ReadCheckTargetType,
        @RequestParam targetId: Int
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        readCheckRemover.remove(targetType, targetId, memberId)
        return ResponseEntity.noContent().build()
    }
}
