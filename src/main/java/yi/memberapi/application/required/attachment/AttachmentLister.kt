package yi.memberapi.application.required.attachment

import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentListResponse

interface AttachmentLister {
    fun list(memberId: Long, page: Int, size: Int): AttachmentListResponse
}
