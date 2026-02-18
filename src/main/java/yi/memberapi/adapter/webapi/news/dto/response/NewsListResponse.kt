package yi.memberapi.adapter.webapi.news.dto.response

data class NewsListResponse(
    val news: List<NewsResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
