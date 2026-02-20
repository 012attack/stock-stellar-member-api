package yi.memberapi.adapter.webapi.newsgroup.dto.response

data class NewsGroupListResponse(
    val newsGroups: List<NewsGroupResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
