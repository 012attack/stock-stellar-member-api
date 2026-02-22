package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.importance.dto.request.SetImportanceRequest
import yi.memberapi.adapter.webapi.importance.dto.response.ImportanceResponse

interface ImportanceSetter {
    fun set(request: SetImportanceRequest, memberId: Long): ImportanceResponse
}
