package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.NewsGroupNewsRemover

@RestController
@RequestMapping("/api/news-groups")
class RemoveNewsGroupNewsApi(
    private val newsGroupNewsRemover: NewsGroupNewsRemover
) {
    @DeleteMapping("/{groupId}/news/{newsId}")
    fun removeNews(
        @PathVariable groupId: Int,
        @PathVariable newsId: Int
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        newsGroupNewsRemover.remove(groupId, newsId, memberId)
        return ResponseEntity.noContent().build()
    }
}
