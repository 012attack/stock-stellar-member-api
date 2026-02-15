package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.dto.request.UpdateOpinionRequest
import yi.memberapi.adapter.webapi.dto.response.OpinionResponse
import yi.memberapi.application.required.OpinionUpdater

@RestController
@RequestMapping("/api/daily-top30-records")
class UpdateRecordOpinionApi(
    private val opinionUpdater: OpinionUpdater
) {

    @PutMapping("/{recordId}/opinions/{opinionId}")
    fun updateOpinion(
        @PathVariable recordId: Int,
        @PathVariable opinionId: Int,
        @RequestBody request: UpdateOpinionRequest
    ): ResponseEntity<OpinionResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = opinionUpdater.update(opinionId, request, memberId)
        return ResponseEntity.ok(response)
    }
}
