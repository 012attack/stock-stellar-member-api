package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.dto.response.OpinionListResponse
import yi.memberapi.application.required.OpinionLister
import yi.memberapi.domain.opinion.TargetType

@RestController
class GetRecordOpinionListApi(
    private val opinionLister: OpinionLister
) {

    @GetMapping("/api/daily-top30-records/{recordId}/opinions")
    fun getOpinionsByRecord(
        @PathVariable recordId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<OpinionListResponse> {
        val response = opinionLister.listByTarget(TargetType.RECORD, recordId, page, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/opinions/records")
    fun getAllRecordOpinions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<OpinionListResponse> {
        val response = opinionLister.listByTargetType(TargetType.RECORD, page, size)
        return ResponseEntity.ok(response)
    }
}
