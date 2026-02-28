package yi.memberapi.adapter.webapi.attachment

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse
import yi.memberapi.application.required.attachment.AttachmentUploader
import java.net.URI

@RestController
@RequestMapping("/api/attachments")
class UploadAttachmentApi(
    private val attachmentUploader: AttachmentUploader
) {

    @PostMapping
    fun upload(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<AttachmentResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = attachmentUploader.upload(file, memberId)
        return ResponseEntity.created(URI.create("/api/attachments/${response.id}")).body(response)
    }
}
