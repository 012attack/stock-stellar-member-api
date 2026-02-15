package yi.memberapi.application.news.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.NewsListResponse
import yi.memberapi.adapter.webapi.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.dto.response.PressResponse
import yi.memberapi.adapter.webapi.dto.response.ThemeResponse
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.required.NewsLister

@Service
@Transactional(readOnly = true)
class QueryNewsLister(
    private val newsRepository: NewsRepository
) : NewsLister {

    override fun list(page: Int, size: Int, title: String?, pressName: String?, themeName: String?): NewsListResponse {
        val pageable = PageRequest.of(page, size)
        val newsPage = newsRepository.findWithFilters(title, pressName, themeName, pageable)

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
