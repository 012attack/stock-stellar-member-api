package yi.memberapi.application.impl.newsgroup.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.news.dto.response.PressResponse
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupDetailResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.NewsGroupRepository
import yi.memberapi.application.required.NewsGroupFinder
import yi.memberapi.domain.newsgroup.NewsGroup

@Service
@Transactional(readOnly = true)
class QueryNewsGroupFinder(
    private val newsGroupRepository: NewsGroupRepository
) : NewsGroupFinder {

    override fun findEntityByIdWithMember(id: Int): NewsGroup {
        return newsGroupRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("NewsGroup not found: $id")
    }

    override fun findEntityByIdWithNews(id: Int): NewsGroup {
        return newsGroupRepository.findByIdWithNews(id)
            ?: throw IllegalArgumentException("NewsGroup not found: $id")
    }

    override fun find(id: Int, memberId: Long): NewsGroupDetailResponse {
        val newsGroup = newsGroupRepository.findByIdWithNews(id)
            ?: throw IllegalArgumentException("NewsGroup not found: $id")

        if (newsGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to view this news group")
        }

        return NewsGroupDetailResponse(
            id = newsGroup.id!!,
            title = newsGroup.title,
            description = newsGroup.description,
            news = newsGroup.news.map { news ->
                NewsResponse(
                    id = news.id!!,
                    title = news.title,
                    link = news.link,
                    press = news.press?.let { press ->
                        PressResponse(
                            id = press.id!!,
                            name = press.name
                        )
                    },
                    themes = news.themes.map { theme ->
                        ThemeResponse(
                            id = theme.id!!,
                            themeName = theme.themeName
                        )
                    },
                    createdAt = news.createdAt
                )
            },
            createdAt = newsGroup.createdAt,
            updatedAt = newsGroup.updatedAt
        )
    }
}
