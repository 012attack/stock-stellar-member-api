package yi.memberapi.application.impl.news.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.dto.response.PressResponse
import yi.memberapi.adapter.webapi.dto.response.ThemeResponse
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.required.NewsFinder
import yi.memberapi.domain.news.News

@Service
@Transactional(readOnly = true)
class QueryNewsFinder(
    private val newsRepository: NewsRepository
) : NewsFinder {

    override fun findEntityByIdWithPressAndThemes(id: Int): News {
        return newsRepository.findByIdWithPressAndThemes(id)
            ?: throw IllegalArgumentException("News not found: $id")
    }

    override fun findById(id: Int): NewsResponse? {
        return newsRepository.findByIdWithPressAndThemes(id)?.let { news ->
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
    }
}
