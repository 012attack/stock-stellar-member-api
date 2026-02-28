package yi.memberapi.adapter.webapi.attachment

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.core.io.Resource
import yi.memberapi.application.required.attachment.AttachmentDownloader
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/api/attachments")
class DownloadAttachmentApi(
    private val attachmentDownloader: AttachmentDownloader
) {

    @GetMapping("/{id}/download")
    fun download(@PathVariable id: Int): ResponseEntity<Resource> {
        val result = attachmentDownloader.download(id)

        val encodedFileName = URLEncoder.encode(result.originalFileName, StandardCharsets.UTF_8)
            .replace("+", "%20")

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(result.contentType ?: "application/octet-stream"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''$encodedFileName")
            .body(result.resource)
    }
}
