package com.astashin.bookvoed

import com.astashin.bookvoed.exceptions.FileNotFoundException
import com.astashin.bookvoed.exceptions.FileStorageException
import com.astashin.bookvoed.properties.AvatarStorageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class AvatarStorageService @Autowired constructor(avatarStorageProperties: AvatarStorageProperties) {

    final var avatarStorageLocation: Path = Paths.get(avatarStorageProperties.uploadDir!!)
            .toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(avatarStorageLocation)
        } catch (ex: Exception) {
            throw Exception("Could not create the directory where the uploaded files will be stored.", ex)
        }
    }

    fun storeAvatar(file: MultipartFile, userName: String): String {
        try {
            val fileExtension = file.originalFilename?.split('.')?.last()
            val filename = "$userName.$fileExtension"
            val targetLocation: Path = avatarStorageLocation.resolve(filename)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return filename
        } catch (e: Exception) {
            throw FileStorageException("Could not store avatar. Please try again!", e)
        }
    }

    fun loadAvatarAsResource(fileName: String): Resource? {
        return try {
            val filePath: Path = avatarStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw FileNotFoundException("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw FileNotFoundException("File not found $fileName", ex)
        }
    }
}