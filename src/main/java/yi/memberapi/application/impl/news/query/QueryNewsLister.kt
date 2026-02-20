package yi.memberapi.application.impl.news.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.news.dto.response.NewsListResponse
import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.news.dto.response.PressResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.required.NewsLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryNewsLister(
    private val newsRepository: NewsRepository,
    private val favoriteRepository: FavoriteRepository
) : NewsLister {

    override fun list(page: Int, size: Int, title: String?, pressName: String?, themeName: String?, favoriteOnly: Boolean, memberId: Long?): NewsListResponse {
        val pageable = PageRequest.of(page, size)

        val newsPage = if (favoriteOnly && memberId != null) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.NEWS)
            if (favoriteIds.isEmpty()) return NewsListResponse(news = emptyList(), page = page, size = size, totalElements = 0, totalPages = 0)
            newsRepository.findWithFiltersByFavoriteIds(title, pressName, themeName, favoriteIds, pageable)
        } else {
            newsRepository.findWithFilters(title, pressName, themeName, pageable)
        }

        val newsList = newsPage.content.map { news ->
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
        }

        return NewsListResponse(
            news = newsList,
            page = newsPage.number,
            size = newsPage.size,
            totalElements = newsPage.totalElements,
            totalPages = newsPage.totalPages
        )
    }
}
