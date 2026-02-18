package yi.memberapi.adapter.webapi.news

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.news.dto.response.NewsListResponse
import yi.memberapi.application.required.NewsLister

@RestController
@RequestMapping("/api/news")
class GetNewsListApi(
    private val newsLister: NewsLister
) {

    @GetMapping
    fun getNews(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) pressName: String?,
        @RequestParam(required = false) themeName: String?
    ): ResponseEntity<NewsListResponse> {
        val response = newsLister.list(page, size, title, pressName, themeName)
        return ResponseEntity.ok(response)
    }
}
