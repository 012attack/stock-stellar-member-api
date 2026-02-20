package yi.memberapi.application.impl.newsgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.newsgroup.dto.request.UpdateNewsGroupRequest
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse
import yi.memberapi.application.required.NewsGroupFinder
import yi.memberapi.application.required.NewsGroupUpdater

@Service
@Transactional
class CommandNewsGroupUpdater(
    private val newsGroupFinder: NewsGroupFinder
) : NewsGroupUpdater {

    override fun update(id: Int, request: UpdateNewsGroupRequest, memberId: Long): NewsGroupResponse {
        val newsGroup = newsGroupFinder.findEntityByIdWithMember(id)

        if (newsGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to update this news group")
        }

        newsGroup.title = request.title
        newsGroup.description = request.description

        return NewsGroupResponse(
            id = newsGroup.id!!,
            title = newsGroup.title,
            description = newsGroup.description,
            newsCount = newsGroup.news.size,
            createdAt = newsGroup.createdAt,
            updatedAt = newsGroup.updatedAt
        )
    }
}
