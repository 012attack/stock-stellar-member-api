package yi.memberapi.domain.attachment

enum class FileType(val extensions: Set<String>) {
    IMAGE(setOf("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico", "tiff")),
    DOCUMENT(setOf("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv", "hwp", "hwpx")),
    VIDEO(setOf("mp4", "avi", "mov", "wmv", "flv", "mkv", "webm")),
    AUDIO(setOf("mp3", "wav", "flac", "aac", "ogg", "wma")),
    ARCHIVE(setOf("zip", "rar", "7z", "tar", "gz")),
    OTHER(emptySet());

    companion object {
        fun fromExtension(extension: String?): FileType {
            if (extension.isNullOrBlank()) return OTHER
            val ext = extension.lowercase()
            return entries.firstOrNull { it.extensions.contains(ext) } ?: OTHER
        }
    }
}
