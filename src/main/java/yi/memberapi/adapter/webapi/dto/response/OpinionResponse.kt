package yi.memberapi.adapter.webapi.dto.response

import java.time.LocalDateTime

data class OpinionResponse(
    val id: Int,
    val title: String,
    val content: String,
    val memberName: String,
    val createdAt: LocalDateTime?,
    val targetType: String,
    val targetId: Int
)
