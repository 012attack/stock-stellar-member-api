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
@RequestMapping("/api/news")
class CreateNewsOpinionApi(
    private val opinionCreator: OpinionCreator
) {

    @PostMapping("/{newsId}/opinions")
    fun createOpinion(
        @PathVariable newsId: Int,
        @RequestBody request: CreateOpinionRequest
    ): ResponseEntity<OpinionResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = opinionCreator.create(request, TargetType.NEWS, newsId, memberId)
        return ResponseEntity.created(URI.create("/api/news/$newsId/opinions/${response.id}")).body(response)
    }
}
