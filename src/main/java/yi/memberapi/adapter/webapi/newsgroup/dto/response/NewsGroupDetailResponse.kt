package yi.memberapi.adapter.webapi.newsgroup.dto.response

import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import java.time.LocalDateTime

data class NewsGroupDetailResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val news: List<NewsResponse>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
