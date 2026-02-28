package yi.memberapi.adapter.webapi.attachment

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.attachment.dto.response.AttachmentListResponse
import yi.memberapi.application.required.attachment.AttachmentLister

@RestController
@RequestMapping("/api/attachments")
class GetAttachmentListApi(
    private val attachmentLister: AttachmentLister
) {

    @GetMapping
    fun getAttachments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<AttachmentListResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = attachmentLister.list(memberId, page, size)
        return ResponseEntity.ok(response)
    }
}
