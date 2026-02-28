package yi.memberapi.application.impl.attachment.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse
import yi.memberapi.application.provided.AttachmentRepository
import yi.memberapi.application.required.attachment.AttachmentUploader
import yi.memberapi.application.required.auth.MemberFinder
import yi.memberapi.common.service.FileStorageService
import yi.memberapi.domain.attachment.Attachment
import yi.memberapi.domain.attachment.FileType

@Service
@Transactional
class CommandAttachmentUploader(
    private val attachmentRepository: AttachmentRepository,
    private val memberFinder: MemberFinder,
    private val fileStorageService: FileStorageService
) : AttachmentUploader {

    override fun upload(file: MultipartFile, memberId: Long): AttachmentResponse {
        val member = memberFinder.findById(memberId)

        val originalFileName = file.originalFilename
            ?: throw IllegalArgumentException("파일명이 없습니다.")
        val extension = originalFileName.substringAfterLast('.', "")
        val fileType = FileType.fromExtension(extension)

        val storedFileInfo = fileStorageService.store(file)

        val attachment = Attachment(
            originalFileName = originalFileName,
            storedFileName = storedFileInfo.storedFileName,
            filePath = storedFileInfo.filePath,
            fileSize = file.size,
            contentType = file.contentType,
            fileType = fileType,
            member = member
        )

        val saved = attachmentRepository.save(attachment)

        return AttachmentResponse(
            id = saved.id!!,
            originalFileName = saved.originalFileName,
            filePath = fileStorageService.getAbsolutePath(saved.filePath),
            fileSize = saved.fileSize,
            contentType = saved.contentType,
            fileType = saved.fileType.name,
            createdAt = saved.createdAt
        )
    }
}
