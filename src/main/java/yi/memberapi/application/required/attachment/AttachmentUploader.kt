package yi.memberapi.application.required.attachment

import org.springframework.web.multipart.MultipartFile
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse

interface AttachmentUploader {
    fun upload(file: MultipartFile, memberId: Long): AttachmentResponse
}
