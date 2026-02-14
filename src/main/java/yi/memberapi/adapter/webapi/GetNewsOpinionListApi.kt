package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.dto.response.OpinionListResponse
import yi.memberapi.application.required.OpinionLister
import yi.memberapi.domain.opinion.TargetType

@RestController
class GetNewsOpinionListApi(
    private val opinionLister: OpinionLister
) {

    @GetMapping("/api/news/{newsId}/opinions")
    fun getOpinionsByNews(
        @PathVariable newsId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<OpinionListResponse> {
        val response = opinionLister.listByTarget(TargetType.NEWS, newsId, page, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/opinions/news")
    fun getAllNewsOpinions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<OpinionListResponse> {
        val response = opinionLister.listByTargetType(TargetType.NEWS, page, size)
        return ResponseEntity.ok(response)
    }
}
