package br.com.acgj.shotit.infra.upload.queue

import br.com.acgj.shotit.domain.Video
import br.com.acgj.shotit.infra.queue.RabbitMQConfiguration
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.io.Serializable
import java.time.LocalDateTime

data class CreatedVideo(
    val id: Long,
    val url: String,
    val timestamp: String
) : Serializable

@Service
class UploadVideoProducer(private val template: RabbitTemplate){

    fun createdVideo(video: Video){
        template.convertAndSend(
            RabbitMQConfiguration.EXCHANGE_NAME,
            RabbitMQConfiguration.UPLOAD_ROUTING_KEY,
            CreatedVideo(video.id!!, video.url!!, LocalDateTime.now().toString())
        )
    }

}
