package yi.memberapi.adapter.webapi.attachment.dto.response

import java.time.LocalDateTime

data class AttachmentResponse(
    val id: Int,
    val originalFileName: String,
    val filePath: String,
    val fileSize: Long,
    val contentType: String?,
    val fileType: String,
    val createdAt: LocalDateTime?
)
