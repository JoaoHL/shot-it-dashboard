package br.com.acgj.shotit.core.videos.events

import br.com.acgj.shotit.core.domain.Thumbnail
import br.com.acgj.shotit.core.domain.ThumbnailRepository
import br.com.acgj.shotit.core.domain.VideoRepository
import br.com.acgj.shotit.core.infra.messaging.RabbitMQVideoConfiguration
import br.com.acgj.shotit.utils.parse
import jakarta.transaction.Transactional
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class VideoThumbnailConsumer(val videoRepository: VideoRepository, val thumbnailRepository: ThumbnailRepository) {

    @RabbitListener(queues = [RabbitMQVideoConfiguration.THUMBNAIL_QUEUE])
    fun handleListenToProcessedThumbnail(message: Message) {
        val body = String(message.body)
        val parsed = parse(body, UploadedThumbnailEvent::class.java)
        handleAddThumbnails(parsed)
    }

    @Transactional
    fun handleAddThumbnails(message: UploadedThumbnailEvent){
        val video = videoRepository.getReferenceById(message.video)
        val thumbnails = message.urls.map { Thumbnail(video = video, url = it ) }
        thumbnails.firstOrNull()?.principal = true
        thumbnailRepository.saveAll(thumbnails)
    }
}
