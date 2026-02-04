package yi.memberapi.application.news.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.dto.response.PressResponse
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.required.NewsFinder

@Service
@Transactional(readOnly = true)
class QueryNewsFinder(
    private val newsRepository: NewsRepository
) : NewsFinder {

    override fun findById(id: Int): NewsResponse? {
        return newsRepository.findByIdWithPress(id)?.let { news ->
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
                createdAt = news.createdAt
            )
        }
    }
}
