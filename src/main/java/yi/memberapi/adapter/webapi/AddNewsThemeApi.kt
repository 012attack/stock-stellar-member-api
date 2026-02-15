package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.dto.request.AddNewsThemeRequest
import yi.memberapi.application.required.NewsThemeAdder

@RestController
@RequestMapping("/api/news")
class AddNewsThemeApi(
    private val newsThemeAdder: NewsThemeAdder
) {

    @PostMapping("/{newsId}/themes")
    fun addThemes(
        @PathVariable newsId: Int,
        @RequestBody request: AddNewsThemeRequest
    ): ResponseEntity<Void> {
        newsThemeAdder.add(newsId, request.themeIds)
        return ResponseEntity.ok().build()
    }
}
