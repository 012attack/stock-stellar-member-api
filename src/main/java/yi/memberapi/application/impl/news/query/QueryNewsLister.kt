package yi.memberapi.application.impl.news.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.news.dto.response.NewsListResponse
import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.news.dto.response.PressResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.ImportanceRepository
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.provided.ReadCheckRepository
import yi.memberapi.application.required.news.NewsLister
import yi.memberapi.domain.favorite.FavoriteTargetType
import yi.memberapi.domain.importance.ImportanceTargetType
import yi.memberapi.domain.readcheck.ReadCheckTargetType
import yi.memberapi.domain.readcheck.ReadFilter
import java.math.BigDecimal

@Service
@Transactional(readOnly = true)
class QueryNewsLister(
    private val newsRepository: NewsRepository,
    private val favoriteRepository: FavoriteRepository,
    private val importanceRepository: ImportanceRepository,
    private val readCheckRepository: ReadCheckRepository
) : NewsLister {

    override fun list(page: Int, size: Int, title: String?, pressName: String?, themeName: String?, favoriteOnly: Boolean, memberId: Long?, minScore: BigDecimal?, readFilter: ReadFilter?): NewsListResponse {
        val pageable = PageRequest.of(page, size)

        val favoriteIds = if (favoriteOnly && memberId != null) {
            favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.NEWS)
        } else null

        val importanceIds = if (minScore != null && memberId != null) {
            importanceRepository.findTargetIdsByMemberIdAndTargetTypeAndMinScore(memberId, ImportanceTargetType.NEWS, minScore)
        } else null

        val baseFilteredIds = when {
            favoriteIds != null && importanceIds != null -> favoriteIds.intersect(importanceIds.toSet()).toList()
            favoriteIds != null -> favoriteIds
            importanceIds != null -> importanceIds
            else -> null
        }

        val emptyResponse = NewsListResponse(news = emptyList(), page = page, size = size, totalElements = 0, totalPages = 0)

        var filteredIds = baseFilteredIds
        var excludeIds: List<Int>? = null

        if (readFilter != null && memberId != null) {
            val readIds = readCheckRepository.findTargetIdsByMemberIdAndTargetType(memberId, ReadCheckTargetType.NEWS)
            when (readFilter) {
                ReadFilter.READ -> {
                    filteredIds = if (filteredIds != null) {
                        filteredIds.intersect(readIds.toSet()).toList()
                    } else {
                        readIds
                    }
                }
                ReadFilter.UNREAD -> {
                    if (filteredIds != null) {
                        filteredIds = filteredIds.minus(readIds.toSet())
                    } else {
                        excludeIds = readIds
                    }
                }
            }
        }

        val newsPage = if (filteredIds != null) {
            if (filteredIds.isEmpty()) return emptyResponse
            newsRepository.findWithFiltersByFavoriteIds(title, pressName, themeName, filteredIds, pageable)
        } else if (excludeIds != null) {
            if (excludeIds.isEmpty()) {
                newsRepository.findWithFilters(title, pressName, themeName, pageable)
            } else {
                newsRepository.findWithFiltersByExcludeIds(title, pressName, themeName, excludeIds, pageable)
            }
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
