package yi.memberapi.adapter.webapi.attachment

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentResponse
import yi.memberapi.application.required.attachment.AttachmentFinder

@RestController
@RequestMapping("/api/attachments")
class GetAttachmentDetailApi(
    private val attachmentFinder: AttachmentFinder
) {

    @GetMapping("/{id}")
    fun getAttachment(@PathVariable id: Int): ResponseEntity<AttachmentResponse> {
        val response = attachmentFinder.findById(id)
        return ResponseEntity.ok(response)
    }
}
