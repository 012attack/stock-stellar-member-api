package yi.memberapi.adapter.webapi.opinion

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.opinion.dto.response.OpinionListResponse
import yi.memberapi.application.required.opinion.OpinionLister
import yi.memberapi.domain.opinion.TargetType
import yi.memberapi.domain.readcheck.ReadFilter

@RestController
class GetNewsOpinionListApi(
    private val opinionLister: OpinionLister
) {

    @GetMapping("/api/news/{newsId}/opinions")
    fun getOpinionsByNews(
        @PathVariable newsId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) favoriteOnly: Boolean = false,
        @RequestParam(required = false) readFilter: ReadFilter? = null
    ): ResponseEntity<OpinionListResponse> {
        val memberId = if (favoriteOnly || readFilter != null) {
            val memberUserDetails = SecurityContextHolder.getContext().authentication?.principal as? MemberUserDetails
            memberUserDetails?.getMember()?.id
        } else null

        val response = opinionLister.listByTarget(TargetType.NEWS, newsId, page, size, favoriteOnly, memberId, readFilter)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/opinions/news")
    fun getAllNewsOpinions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) favoriteOnly: Boolean = false,
        @RequestParam(required = false) readFilter: ReadFilter? = null
    ): ResponseEntity<OpinionListResponse> {
        val memberId = if (favoriteOnly || readFilter != null) {
            val memberUserDetails = SecurityContextHolder.getContext().authentication?.principal as? MemberUserDetails
            memberUserDetails?.getMember()?.id
        } else null

        val response = opinionLister.listByTargetType(TargetType.NEWS, page, size, favoriteOnly, memberId, readFilter)
        return ResponseEntity.ok(response)
    }
}
