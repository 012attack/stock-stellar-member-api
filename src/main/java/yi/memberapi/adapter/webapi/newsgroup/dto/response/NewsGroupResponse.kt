package yi.memberapi.adapter.webapi.newsgroup.dto.response

import java.time.LocalDateTime

data class NewsGroupResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val newsCount: Int,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
