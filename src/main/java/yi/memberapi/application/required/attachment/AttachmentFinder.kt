package yi.memberapi.application.required.attachment

import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse

interface AttachmentFinder {
    fun findById(id: Int): AttachmentResponse
}
