package yi.memberapi.adapter.webapi.readcheck.dto.response

import java.time.LocalDateTime

data class ReadCheckResponse(
    val id: Int,
    val targetType: String,
    val targetId: Int,
    val createdAt: LocalDateTime?
)
