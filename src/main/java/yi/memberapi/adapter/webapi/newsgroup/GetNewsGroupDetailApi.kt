package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupDetailResponse
import yi.memberapi.application.required.NewsGroupFinder

@RestController
@RequestMapping("/api/news-groups")
class GetNewsGroupDetailApi(
    private val newsGroupFinder: NewsGroupFinder
) {
    @GetMapping("/{id}")
    fun detail(@PathVariable id: Int): ResponseEntity<NewsGroupDetailResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = newsGroupFinder.find(id, memberId)
        return ResponseEntity.ok(response)
    }
}
