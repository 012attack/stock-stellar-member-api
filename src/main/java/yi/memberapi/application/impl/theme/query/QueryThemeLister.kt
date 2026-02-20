package yi.memberapi.application.impl.theme.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeListResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.ThemeRepository
import yi.memberapi.application.required.ThemeLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryThemeLister(
    private val themeRepository: ThemeRepository,
    private val favoriteRepository: FavoriteRepository
) : ThemeLister {

    override fun list(page: Int, size: Int, themeName: String?, favoriteOnly: Boolean, memberId: Long?): ThemeListResponse {
        val pageable = PageRequest.of(page, size)

        val themePage = if (favoriteOnly && memberId != null) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.THEME)
            if (favoriteIds.isEmpty()) return ThemeListResponse(themes = emptyList(), page = page, size = size, totalElements = 0, totalPages = 0)
            themeRepository.findWithFiltersByFavoriteIds(themeName, favoriteIds, pageable)
        } else {
            themeRepository.findWithFilters(themeName, pageable)
        }

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
