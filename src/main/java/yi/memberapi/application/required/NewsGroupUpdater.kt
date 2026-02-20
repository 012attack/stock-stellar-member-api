package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.newsgroup.dto.request.UpdateNewsGroupRequest
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse

interface NewsGroupUpdater {
    fun update(id: Int, request: UpdateNewsGroupRequest, memberId: Long): NewsGroupResponse
}
