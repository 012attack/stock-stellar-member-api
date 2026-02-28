package yi.memberapi.application.required.attachment

import org.springframework.core.io.Resource

data class AttachmentDownloadResult(
    val resource: Resource,
    val originalFileName: String,
    val contentType: String?
)

interface AttachmentDownloader {
    fun download(id: Int): AttachmentDownloadResult
}
