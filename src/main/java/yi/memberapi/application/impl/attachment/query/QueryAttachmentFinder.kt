package yi.memberapi.application.impl.attachment.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse
import yi.memberapi.application.provided.AttachmentRepository
import yi.memberapi.application.required.attachment.AttachmentFinder

@Service
@Transactional(readOnly = true)
class QueryAttachmentFinder(
    private val attachmentRepository: AttachmentRepository
) : AttachmentFinder {

    override fun findById(id: Int): AttachmentResponse {
        val attachment = attachmentRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("첨부파일을 찾을 수 없습니다: $id")

        return AttachmentResponse(
            id = attachment.id!!,
            originalFileName = attachment.originalFileName,
            fileSize = attachment.fileSize,
            contentType = attachment.contentType,
            fileType = attachment.fileType.name,
            createdAt = attachment.createdAt
        )
    }
}
