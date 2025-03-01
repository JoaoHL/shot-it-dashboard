package br.com.acgj.shotit.core.videos.events

import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.core.infra.messaging.RabbitMQVideoConfiguration
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UploadVideoProducer(private val template: RabbitTemplate){
    fun createdVideo(video: Video){
        template.convertAndSend(
            RabbitMQVideoConfiguration.EXCHANGE_NAME,
            RabbitMQVideoConfiguration.UPLOAD_ROUTING_KEY,
            UploadedVideoEvent(video.id!!, video.url!!, LocalDateTime.now().toString())
        )
    }
}
