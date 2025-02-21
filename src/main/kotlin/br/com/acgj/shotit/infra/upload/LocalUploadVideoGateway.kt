package br.com.acgj.shotit.infra.upload

import br.com.acgj.shotit.domain.Video
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

class LocalUploadVideoGateway(
    private val logger: Logger = LoggerFactory.getLogger("LocalUploader"),
    private val loader: ResourceLoader,
    private val folderName: String
) : UploadGateway {

    override fun upload(video: Video): String {
        val resource = Paths.get(folderName)

        if (!Files.exists(resource)){
            Files.createDirectories(resource)
        }

        val name = video.file!!.originalFilename ?: UUID.randomUUID().toString()

        val path = resource.toAbsolutePath().resolve(name)

        video.file?.inputStream?.use {
            Files.copy(it, path, StandardCopyOption.REPLACE_EXISTING)
        }

        logger.info("[UPLOAD_FINISHED]: Created $path")

        return path.toString()
    }


}