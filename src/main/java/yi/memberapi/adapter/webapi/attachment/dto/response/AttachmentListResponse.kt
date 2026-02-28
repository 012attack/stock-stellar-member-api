package yi.memberapi.adapter.webapi.attachment.dto.response

data class AttachmentListResponse(
    val attachments: List<AttachmentResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
