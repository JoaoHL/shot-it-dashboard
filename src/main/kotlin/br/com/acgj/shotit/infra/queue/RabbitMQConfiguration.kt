package br.com.acgj.shotit.infra.queue

import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfiguration {

    companion object {
        const val EXCHANGE_NAME = "video.event"
        const val UPLOAD_QUEUE = "video.processed"
        const val UPLOAD_ROUTING_KEY = "video.upload"
        const val THUMBNAIL_QUEUE = "video.thumbnail"
        const val THUMBNAIL_ROUTING_KEY = "thumbnail.generated"
    }

    @Bean
    fun videoExchange(): Exchange = TopicExchange(EXCHANGE_NAME)

    @Bean
    fun videoUploadQueue(): Queue = Queue(UPLOAD_QUEUE)

    @Bean
    fun videoThumbnailQueue(): Queue = Queue(THUMBNAIL_QUEUE)

    @Bean
    fun videoUploadBinding(videoUploadQueue: Queue, videoExchange: Exchange): Binding =
        BindingBuilder
            .bind(videoUploadQueue)
            .to(videoExchange as TopicExchange)
            .with(UPLOAD_ROUTING_KEY)

    @Bean
    fun videoThumbnailProcessed(videoThumbnailQueue: Queue, videoExchange: Exchange): Binding = BindingBuilder
        .bind(videoThumbnailQueue)
        .to(videoExchange as TopicExchange)
        .with(THUMBNAIL_ROUTING_KEY)

    @Bean
    fun messageConverter(): MessageConverter = Jackson2JsonMessageConverter()
}
