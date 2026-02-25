package yi.memberapi.adapter.webapi.stocknews

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.news.dto.response.PressResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.required.StockFinder

@RestController
@RequestMapping("/api/stocks")
class GetStockNewsListApi(
    private val stockFinder: StockFinder
) {

    @GetMapping("/{stockId}/news")
    fun getNewsList(
        @PathVariable stockId: Int
    ): ResponseEntity<List<NewsResponse>> {
        val stock = stockFinder.findEntityByIdWithNews(stockId)

        val newsResponses = stock.news.map { news ->
            NewsResponse(
                id = news.id!!,
                title = news.title,
                link = news.link,
                press = news.press?.let { PressResponse(it.id!!, it.name) },
                themes = emptyList(),
                createdAt = news.createdAt
            )
        }

        return ResponseEntity.ok(newsResponses)
    }
}
