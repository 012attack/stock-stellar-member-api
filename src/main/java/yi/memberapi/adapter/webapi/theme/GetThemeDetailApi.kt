package yi.memberapi.adapter.webapi.theme

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeDetailResponse
import yi.memberapi.application.required.ThemeFinder

@RestController
@RequestMapping("/api/themes")
class GetThemeDetailApi(
    private val themeFinder: ThemeFinder
) {

    @GetMapping("/{id}")
    fun getThemeDetail(@PathVariable id: Int): ResponseEntity<ThemeDetailResponse> {
        val theme = themeFinder.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(theme)
    }
}
