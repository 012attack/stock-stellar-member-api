package yi.memberapi.adapter.webapi.importance.dto.request

import yi.memberapi.domain.importance.ImportanceTargetType
import java.math.BigDecimal

data class SetImportanceRequest(
    val targetType: ImportanceTargetType,
    val targetId: Int,
    val score: BigDecimal
)
