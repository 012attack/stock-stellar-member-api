package yi.memberapi.common.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import yi.memberapi.common.config.FileStorageProperties
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class StoredFileInfo(
    val storedFileName: String,
    val filePath: String
)

@Service
class FileStorageService(
    private val fileStorageProperties: FileStorageProperties
) {

    private val rootLocation: Path = Paths.get(fileStorageProperties.uploadDir)

    init {
        Files.createDirectories(rootLocation)
    }

    fun store(file: MultipartFile): StoredFileInfo {
        val originalFileName = file.originalFilename
            ?: throw IllegalArgumentException("파일명이 없습니다.")

        val extension = originalFileName.substringAfterLast('.', "")
        val storedFileName = "${UUID.randomUUID()}${if (extension.isNotBlank()) ".$extension" else ""}"

        val dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val targetDir = rootLocation.resolve(dateFolder)
        Files.createDirectories(targetDir)

        val targetPath = targetDir.resolve(storedFileName)
        Files.copy(file.inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)

        return StoredFileInfo(
            storedFileName = storedFileName,
            filePath = "$dateFolder/$storedFileName"
        )
    }

    fun load(filePath: String): Resource {
        val file = rootLocation.resolve(filePath)
        val resource = UrlResource(file.toUri())
        if (!resource.exists() || !resource.isReadable) {
            throw IllegalArgumentException("파일을 찾을 수 없습니다: $filePath")
        }
        return resource
    }

    fun getPublicPath(filePath: String): String {
        return "/uploads/$filePath"
    }

    fun delete(filePath: String) {
        val file = rootLocation.resolve(filePath)
        Files.deleteIfExists(file)
    }
}
