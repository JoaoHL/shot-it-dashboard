package br.com.acgj.shotit.core.videos.events

import br.com.acgj.shotit.core.domain.*
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
        val video = videoRepository.findById(message.video).orElseThrow { NotFoundError() }
        val thumbnails = message.urls.map { Thumbnail(video = video, url = it ) }
        video.status = VideoStatus.SUCCESS
        thumbnails.firstOrNull()?.principal = true
        thumbnailRepository.saveAll(thumbnails)
        videoRepository.save(video)
    }
}
