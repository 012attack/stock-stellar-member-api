package yi.memberapi.adapter.webapi.dto.response

import java.time.LocalDateTime

data class NewsResponse(
    val id: Int,
    val title: String,
    val link: String,
    val press: PressResponse?,
    val themes: List<ThemeResponse> = emptyList(),
    val createdAt: LocalDateTime?
)
