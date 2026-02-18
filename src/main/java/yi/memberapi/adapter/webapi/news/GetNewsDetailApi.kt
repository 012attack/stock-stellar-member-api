package yi.memberapi.adapter.webapi.news

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.application.required.NewsFinder

@RestController
@RequestMapping("/api/news")
class GetNewsDetailApi(
    private val newsFinder: NewsFinder
) {

    @GetMapping("/{id}")
    fun getNewsDetail(@PathVariable id: Int): ResponseEntity<NewsResponse> {
        val news = newsFinder.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(news)
    }
}
