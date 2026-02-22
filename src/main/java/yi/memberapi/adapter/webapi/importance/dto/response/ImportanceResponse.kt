package yi.memberapi.adapter.webapi.importance.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class ImportanceResponse(
    val id: Int,
    val targetType: String,
    val targetId: Int,
    val score: BigDecimal,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
