package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupListResponse
import yi.memberapi.application.required.NewsGroupLister

@RestController
@RequestMapping("/api/news-groups")
class GetNewsGroupListApi(
    private val newsGroupLister: NewsGroupLister
) {
    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) favoriteOnly: Boolean = false
    ): ResponseEntity<NewsGroupListResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = newsGroupLister.list(page, size, memberId, favoriteOnly)
        return ResponseEntity.ok(response)
    }
}
