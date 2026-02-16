package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.ThemeDetailResponse

interface ThemeFinder {
    fun findById(id: Int): ThemeDetailResponse?
}
