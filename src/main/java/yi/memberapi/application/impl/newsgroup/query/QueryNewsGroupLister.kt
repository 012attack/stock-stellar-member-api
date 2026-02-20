package yi.memberapi.application.impl.newsgroup.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupListResponse
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.NewsGroupRepository
import yi.memberapi.application.required.NewsGroupLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryNewsGroupLister(
    private val newsGroupRepository: NewsGroupRepository,
    private val favoriteRepository: FavoriteRepository
) : NewsGroupLister {

    override fun list(page: Int, size: Int, memberId: Long, favoriteOnly: Boolean): NewsGroupListResponse {
        val pageable = PageRequest.of(page, size)

        val newsGroupPage = if (favoriteOnly) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.NEWS_GROUP)
            if (favoriteIds.isEmpty()) {
                return NewsGroupListResponse(
                    newsGroups = emptyList(),
                    page = page,
                    size = size,
                    totalElements = 0,
                    totalPages = 0
                )
            }
            newsGroupRepository.findByMemberIdAndFavoriteIds(memberId, favoriteIds, pageable)
        } else {
            newsGroupRepository.findByMemberId(memberId, pageable)
        }

        val newsGroupList = newsGroupPage.content.map { newsGroup ->
            NewsGroupResponse(
                id = newsGroup.id!!,
                title = newsGroup.title,
                description = newsGroup.description,
                newsCount = newsGroup.news.size,
                createdAt = newsGroup.createdAt,
                updatedAt = newsGroup.updatedAt
            )
        }

        return NewsGroupListResponse(
            newsGroups = newsGroupList,
            page = newsGroupPage.number,
            size = newsGroupPage.size,
            totalElements = newsGroupPage.totalElements,
            totalPages = newsGroupPage.totalPages
        )
    }
}
