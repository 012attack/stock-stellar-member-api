package yi.memberapi.adapter.webapi.newsgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.NewsGroupDeleter

@RestController
@RequestMapping("/api/news-groups")
class DeleteNewsGroupApi(
    private val newsGroupDeleter: NewsGroupDeleter
) {
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        newsGroupDeleter.delete(id, memberId)
        return ResponseEntity.noContent().build()
    }
}
