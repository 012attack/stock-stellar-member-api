package yi.memberapi.adapter.webapi.importance

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.importance.dto.request.SetImportanceRequest
import yi.memberapi.adapter.webapi.importance.dto.response.ImportanceResponse
import yi.memberapi.application.required.ImportanceSetter

@RestController
@RequestMapping("/api/importances")
class SetImportanceApi(
    private val importanceSetter: ImportanceSetter
) {

    @PutMapping
    fun setImportance(@RequestBody request: SetImportanceRequest): ResponseEntity<ImportanceResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = importanceSetter.set(request, memberId)
        return ResponseEntity.ok(response)
    }
}
