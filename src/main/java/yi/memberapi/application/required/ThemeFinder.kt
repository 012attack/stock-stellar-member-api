package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.theme.dto.response.ThemeDetailResponse
import yi.memberapi.domain.theme.Theme

interface ThemeFinder {
    fun findById(id: Int): ThemeDetailResponse?
    fun findAllEntitiesByIds(ids: List<Int>): List<Theme>
}
