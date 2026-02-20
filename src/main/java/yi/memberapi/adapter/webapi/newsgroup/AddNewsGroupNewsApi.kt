package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.newsgroup.dto.request.AddNewsGroupNewsRequest
import yi.memberapi.application.required.NewsGroupNewsAdder

@RestController
@RequestMapping("/api/news-groups")
class AddNewsGroupNewsApi(
    private val newsGroupNewsAdder: NewsGroupNewsAdder
) {
    @PostMapping("/{groupId}/news")
    fun addNews(
        @PathVariable groupId: Int,
        @RequestBody request: AddNewsGroupNewsRequest
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        newsGroupNewsAdder.add(groupId, request.newsIds, memberId)
        return ResponseEntity.ok().build()
    }
}
