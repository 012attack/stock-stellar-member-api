package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.MemberListResponse

interface MemberLister {
    fun list(page: Int, size: Int): MemberListResponse
}
