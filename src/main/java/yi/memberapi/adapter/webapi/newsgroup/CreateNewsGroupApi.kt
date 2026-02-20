package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.newsgroup.dto.request.CreateNewsGroupRequest
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse
import yi.memberapi.application.required.NewsGroupCreator
import java.net.URI

@RestController
@RequestMapping("/api/news-groups")
class CreateNewsGroupApi(
    private val newsGroupCreator: NewsGroupCreator
) {
    @PostMapping
    fun create(@RequestBody request: CreateNewsGroupRequest): ResponseEntity<NewsGroupResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = newsGroupCreator.create(request, memberId)
        return ResponseEntity.created(URI.create("/api/news-groups/${response.id}")).body(response)
    }
}
