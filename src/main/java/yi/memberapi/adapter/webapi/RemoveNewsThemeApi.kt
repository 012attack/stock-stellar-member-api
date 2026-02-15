package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.application.required.NewsThemeRemover

@RestController
@RequestMapping("/api/news")
class RemoveNewsThemeApi(
    private val newsThemeRemover: NewsThemeRemover
) {

    @DeleteMapping("/{newsId}/themes/{themeId}")
    fun removeTheme(
        @PathVariable newsId: Int,
        @PathVariable themeId: Int
    ): ResponseEntity<Void> {
        newsThemeRemover.remove(newsId, themeId)
        return ResponseEntity.noContent().build()
    }
}
