package yi.memberapi.application.impl.attachment.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.AttachmentRepository
import yi.memberapi.application.required.attachment.AttachmentDownloadResult
import yi.memberapi.application.required.attachment.AttachmentDownloader
import yi.memberapi.common.service.FileStorageService

@Service
@Transactional(readOnly = true)
class QueryAttachmentDownloader(
    private val attachmentRepository: AttachmentRepository,
    private val fileStorageService: FileStorageService
) : AttachmentDownloader {

    override fun download(id: Int): AttachmentDownloadResult {
        val attachment = attachmentRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("첨부파일을 찾을 수 없습니다: $id")

        val resource = fileStorageService.load(attachment.filePath)

        return AttachmentDownloadResult(
            resource = resource,
            originalFileName = attachment.originalFileName,
            contentType = attachment.contentType
        )
    }
}
