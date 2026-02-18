package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.theme.dto.response.ThemeListResponse

interface ThemeLister {
    fun list(page: Int, size: Int, themeName: String?): ThemeListResponse
}
