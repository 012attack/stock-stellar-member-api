package yi.memberapi.adapter.webapi.importance

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.ImportanceRemover
import yi.memberapi.domain.importance.ImportanceTargetType

@RestController
@RequestMapping("/api/importances")
class RemoveImportanceApi(
    private val importanceRemover: ImportanceRemover
) {

    @DeleteMapping
    fun removeImportance(
        @RequestParam targetType: ImportanceTargetType,
        @RequestParam targetId: Int
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        importanceRemover.remove(targetType, targetId, memberId)
        return ResponseEntity.noContent().build()
    }
}
