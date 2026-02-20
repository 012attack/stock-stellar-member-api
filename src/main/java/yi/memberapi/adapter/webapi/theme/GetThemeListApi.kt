package yi.memberapi.adapter.webapi.theme

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeListResponse
import yi.memberapi.application.required.ThemeLister

@RestController
@RequestMapping("/api/themes")
class GetThemeListApi(
    private val themeLister: ThemeLister
) {

    @GetMapping
    fun getThemes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) themeName: String?,
        @RequestParam(required = false) favoriteOnly: Boolean = false
    ): ResponseEntity<ThemeListResponse> {
        val memberId = if (favoriteOnly) {
            val memberUserDetails = SecurityContextHolder.getContext().authentication?.principal as? MemberUserDetails
            memberUserDetails?.getMember()?.id
        } else null

        val response = themeLister.list(page, size, themeName, favoriteOnly, memberId)
        return ResponseEntity.ok(response)
    }
}
