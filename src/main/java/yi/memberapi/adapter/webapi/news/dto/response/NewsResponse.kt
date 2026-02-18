package yi.memberapi.adapter.webapi.news.dto.response

import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import java.time.LocalDateTime

data class NewsResponse(
    val id: Int,
    val title: String,
    val link: String,
    val press: PressResponse?,
    val themes: List<ThemeResponse> = emptyList(),
    val createdAt: LocalDateTime?
)
