package br.com.acgj.shotit.presentation.consumers

import br.com.acgj.shotit.domain.Thumbnail
import br.com.acgj.shotit.infra.ThumbnailRepository
import br.com.acgj.shotit.infra.VideoRepository
import br.com.acgj.shotit.infra.queue.RabbitMQConfiguration
import br.com.acgj.shotit.utils.parse
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.transaction.Transactional
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

data class UploadedThumbnail(
    @JsonProperty("video_id") val video : Long,
    @JsonProperty("urls") val urls: List<String>
)

@Service
class VideoThumbnailConsumer(val videoRepository: VideoRepository, val thumbnailRepository: ThumbnailRepository) {

    @RabbitListener(queues = [RabbitMQConfiguration.THUMBNAIL_QUEUE])
    fun handleListenToProcessedThumbnail(message: Message) {
        val body = String(message.body)
        val parsed = parse(body, UploadedThumbnail::class.java)
        handleAddThumbnails(parsed)
    }

    @Transactional
    fun handleAddThumbnails(message: UploadedThumbnail){
        val video = videoRepository.getReferenceById(message.video)
        val thumbnails = message.urls.map { Thumbnail(video = video, url = it ) }
        thumbnails.firstOrNull()?.principal = true
        thumbnailRepository.saveAll(thumbnails)
    }
}
