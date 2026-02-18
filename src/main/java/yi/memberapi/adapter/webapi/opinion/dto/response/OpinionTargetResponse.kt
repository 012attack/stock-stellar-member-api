package yi.memberapi.adapter.webapi.opinion.dto.response

import yi.memberapi.adapter.webapi.news.dto.response.PressResponse
import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import java.time.LocalDate
import java.time.LocalDateTime

data class OpinionTargetResponse(
    val targetType: String,
    val news: OpinionNewsTarget?,
    val record: OpinionRecordTarget?
) {
    data class OpinionNewsTarget(
        val id: Int,
        val title: String,
        val link: String,
        val press: PressResponse?,
        val createdAt: LocalDateTime?
    )

    data class OpinionRecordTarget(
        val id: Int,
        val recordDate: LocalDate,
        val rank: Int,
        val changeRate: String?,
        val stock: StockResponse?,
        val themes: List<ThemeResponse>
    )
}
