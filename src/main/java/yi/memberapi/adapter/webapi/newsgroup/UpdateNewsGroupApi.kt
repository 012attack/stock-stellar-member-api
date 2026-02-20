package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.newsgroup.dto.request.UpdateNewsGroupRequest
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse
import yi.memberapi.application.required.NewsGroupUpdater

@RestController
@RequestMapping("/api/news-groups")
class UpdateNewsGroupApi(
    private val newsGroupUpdater: NewsGroupUpdater
) {
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody request: UpdateNewsGroupRequest
    ): ResponseEntity<NewsGroupResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = newsGroupUpdater.update(id, request, memberId)
        return ResponseEntity.ok(response)
    }
}
