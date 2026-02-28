package yi.memberapi.application.required.attachment

interface AttachmentDeleter {
    fun delete(id: Int, memberId: Long)
}
