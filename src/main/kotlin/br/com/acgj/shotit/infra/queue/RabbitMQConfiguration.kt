package br.com.acgj.shotit.infra.queue

import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfiguration {

    @Bean
    fun userExchange(): Exchange = TopicExchange("user.exchange")

    @Bean
    fun userQueue(): Queue = Queue("user.queue")

    @Bean
    fun userBinding(userQueue: Queue, userExchange: Exchange ): Binding {
        val binding = BindingBuilder
            .bind(userQueue)
            .to(userExchange as TopicExchange)
            .with("user.created")

        return binding
    }

    @Bean
    fun messageConverter(): MessageConverter = Jackson2JsonMessageConverter()
}
