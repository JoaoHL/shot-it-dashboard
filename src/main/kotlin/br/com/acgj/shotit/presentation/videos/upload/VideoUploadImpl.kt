package br.com.acgj.shotit.presentation.videos.upload

import br.com.acgj.shotit.application.UploadVideoService
import br.com.acgj.shotit.domain.Video
import br.com.acgj.shotit.infra.VideoRepository
import br.com.acgj.shotit.infra.upload.UploadGateway
import br.com.acgj.shotit.infra.upload.queue.UploadVideoProducer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class VideoUploadImpl(
    private val repository: VideoRepository,
    private val gateway: UploadGateway,
    private val producer: UploadVideoProducer,
    private val logger: Logger = LoggerFactory.getLogger("VideoUploader")
) : UploadVideoService {

    override suspend fun upload(videos: List<Video>) {
        videos.forEach { upload(it) }
    }

    suspend fun upload(video: Video) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = gateway.upload(video)
                video.url = url
                repository.save(video)
                producer.createdVideo(video)
            } catch (exception: Exception) {
                logger.info("Failed to upload, reason was: ${exception.message}")
            }
        }
    }

    override suspend fun retrieve(key: String): ByteArrayOutputStream {
        return gateway.retrieve(key)
    }
}