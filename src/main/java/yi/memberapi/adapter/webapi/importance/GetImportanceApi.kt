package yi.memberapi.adapter.webapi.importance

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.importance.dto.response.ImportanceResponse
import yi.memberapi.application.required.ImportanceGetter
import yi.memberapi.domain.importance.ImportanceTargetType

@RestController
@RequestMapping("/api/importances")
class GetImportanceApi(
    private val importanceGetter: ImportanceGetter
) {

    @GetMapping
    fun getImportance(
        @RequestParam targetType: ImportanceTargetType,
        @RequestParam targetId: Int
    ): ResponseEntity<ImportanceResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = importanceGetter.get(targetType, targetId, memberId)
        return ResponseEntity.ok(response)
    }
}
