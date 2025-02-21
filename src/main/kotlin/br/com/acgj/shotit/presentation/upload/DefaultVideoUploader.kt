package br.com.acgj.shotit.presentation.upload

import br.com.acgj.shotit.application.UploadVideoService
import br.com.acgj.shotit.domain.Video
import br.com.acgj.shotit.infra.VideoRepository
import br.com.acgj.shotit.infra.upload.UploadGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DefaultVideoUploader(
    private val repository: VideoRepository,
    private val gateway: UploadGateway,
    private val logger: Logger = LoggerFactory.getLogger("VideoUploader")
) : UploadVideoService {

    override fun upload(videos: List<Video>) {
        videos.forEach { upload(it) }
    }

    fun upload(video: Video) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = gateway.upload(video)
                video.url = url
                repository.save(video)
            }
            catch (exception: Exception){
                logger.info("Failed to upload, reason was: ${exception.message}")
            }
        }
    }
}