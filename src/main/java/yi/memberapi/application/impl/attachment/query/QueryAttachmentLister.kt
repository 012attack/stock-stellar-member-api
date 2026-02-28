package yi.memberapi.application.impl.attachment.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentListResponse
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse
import yi.memberapi.application.provided.AttachmentRepository
import yi.memberapi.application.required.attachment.AttachmentLister

@Service
@Transactional(readOnly = true)
class QueryAttachmentLister(
    private val attachmentRepository: AttachmentRepository
) : AttachmentLister {

    override fun list(memberId: Long, page: Int, size: Int): AttachmentListResponse {
        val pageable = PageRequest.of(page, size)
        val attachmentPage = attachmentRepository.findByMemberId(memberId, pageable)

        val attachments = attachmentPage.content.map { attachment ->
            AttachmentResponse(
                id = attachment.id!!,
                originalFileName = attachment.originalFileName,
                fileSize = attachment.fileSize,
                contentType = attachment.contentType,
                fileType = attachment.fileType.name,
                createdAt = attachment.createdAt
            )
        }

        return AttachmentListResponse(
            attachments = attachments,
            page = attachmentPage.number,
            size = attachmentPage.size,
            totalElements = attachmentPage.totalElements,
            totalPages = attachmentPage.totalPages
        )
    }
}
