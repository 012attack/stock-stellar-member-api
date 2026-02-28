package yi.memberapi.application.required.readcheck

import yi.memberapi.adapter.webapi.readcheck.dto.request.MarkReadCheckRequest
import yi.memberapi.adapter.webapi.readcheck.dto.response.ReadCheckResponse

interface ReadCheckMarker {
    fun mark(request: MarkReadCheckRequest, memberId: Long): ReadCheckResponse
}
