package yi.memberapi.application.impl.theme.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeListResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.ThemeRepository
import yi.memberapi.application.required.ThemeLister

@Service
@Transactional(readOnly = true)
class QueryThemeLister(
    private val themeRepository: ThemeRepository
) : ThemeLister {

    override fun list(page: Int, size: Int, themeName: String?): ThemeListResponse {
        val pageable = PageRequest.of(page, size)
        val themePage = themeRepository.findWithFilters(themeName, pageable)

        val themeList = themePage.content.map { theme ->
            ThemeResponse(
                id = theme.id!!,
                themeName = theme.themeName
            )
        }

        return ThemeListResponse(
            themes = themeList,
            page = themePage.number,
            size = themePage.size,
            totalElements = themePage.totalElements,
            totalPages = themePage.totalPages
        )
    }
}
