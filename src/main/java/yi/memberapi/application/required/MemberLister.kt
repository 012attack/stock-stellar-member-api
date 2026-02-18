package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.auth.dto.response.MemberListResponse

interface MemberLister {
    fun list(page: Int, size: Int): MemberListResponse
}
