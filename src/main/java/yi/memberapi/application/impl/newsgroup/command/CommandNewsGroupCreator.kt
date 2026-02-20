package yi.memberapi.application.impl.newsgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.newsgroup.dto.request.CreateNewsGroupRequest
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse
import yi.memberapi.application.provided.NewsGroupRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.application.required.NewsGroupCreator
import yi.memberapi.domain.newsgroup.NewsGroup

@Service
@Transactional
class CommandNewsGroupCreator(
    private val newsGroupRepository: NewsGroupRepository,
    private val memberFinder: MemberFinder
) : NewsGroupCreator {

    override fun create(request: CreateNewsGroupRequest, memberId: Long): NewsGroupResponse {
        val member = memberFinder.findById(memberId)
        val newsGroup = NewsGroup(
            title = request.title,
            description = request.description,
            member = member
        )
        val saved = newsGroupRepository.save(newsGroup)
        return NewsGroupResponse(
            id = saved.id!!,
            title = saved.title,
            description = saved.description,
            newsCount = 0,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
    }
}
