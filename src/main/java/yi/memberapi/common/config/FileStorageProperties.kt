package yi.memberapi.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file-storage")
data class FileStorageProperties(
    val uploadDir: String = "uploads"
)
