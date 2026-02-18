package yi.memberapi.adapter.webapi.opinion

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.opinion.dto.request.CreateOpinionRequest
import yi.memberapi.adapter.webapi.opinion.dto.response.OpinionResponse
import yi.memberapi.application.required.OpinionCreator
import yi.memberapi.domain.opinion.TargetType
import java.net.URI

@RestController
@RequestMapping("/api/daily-top30-records")
class CreateRecordOpinionApi(
    private val opinionCreator: OpinionCreator
) {

    @PostMapping("/{recordId}/opinions")
    fun createOpinion(
        @PathVariable recordId: Int,
        @RequestBody request: CreateOpinionRequest
    ): ResponseEntity<OpinionResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = opinionCreator.create(request, TargetType.RECORD, recordId, memberId)
        return ResponseEntity.created(URI.create("/api/daily-top30-records/$recordId/opinions/${response.id}")).body(response)
    }
}
