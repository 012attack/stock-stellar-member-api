package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.newsgroup.dto.request.CreateNewsGroupRequest
import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupResponse

interface NewsGroupCreator {
    fun create(request: CreateNewsGroupRequest, memberId: Long): NewsGroupResponse
}
