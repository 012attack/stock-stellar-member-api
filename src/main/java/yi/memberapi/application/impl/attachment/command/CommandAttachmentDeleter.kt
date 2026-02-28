package yi.memberapi.application.impl.attachment.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.AttachmentRepository
import yi.memberapi.application.required.attachment.AttachmentDeleter
import yi.memberapi.common.service.FileStorageService

@Service
@Transactional
class CommandAttachmentDeleter(
    private val attachmentRepository: AttachmentRepository,
    private val fileStorageService: FileStorageService
) : AttachmentDeleter {

    override fun delete(id: Int, memberId: Long) {
        val attachment = attachmentRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("첨부파일을 찾을 수 없습니다: $id")

        if (attachment.member?.id != memberId) {
            throw IllegalStateException("본인이 업로드한 파일만 삭제할 수 있습니다.")
        }

        fileStorageService.delete(attachment.filePath)
        attachmentRepository.delete(attachment)
    }
}
