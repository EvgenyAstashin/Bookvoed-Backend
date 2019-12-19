package com.astashin.bookvoed

import com.astashin.bookvoed.exceptions.FileNotFoundException
import com.astashin.bookvoed.exceptions.FileStorageException
import com.astashin.bookvoed.properties.AvatarStorageProperties
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class AvatarStorageService @Autowired constructor(avatarStorageProperties: AvatarStorageProperties) {

    private final var avatarStorageLocation: Path = Paths.get(avatarStorageProperties.uploadDir!!)
            .toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(avatarStorageLocation)
        } catch (ex: Exception) {
            throw Exception("Could not create the directory where the uploaded files will be stored.", ex)
        }
    }

    fun storeAvatar(avatarBase64: String, extension: String, userName: String): String {
        try {
            val imageBytes: ByteArray = Base64.decodeBase64(avatarBase64)
            val filename = "$userName.$extension"
            val targetLocation: Path = avatarStorageLocation.resolve(filename)
            Files.copy(ByteArrayInputStream(imageBytes), targetLocation, StandardCopyOption.REPLACE_EXISTING)
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