package br.com.acgj.shotit.presentation.authentication

import br.com.acgj.shotit.domain.User
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class AuthenticationProducer(private val template: RabbitTemplate) {

    fun sendCreateUserEvent(user: User){
        val event = mapOf("hello" to "world")
        template.convertAndSend(
            "user.exchange",
            "user.created",
            event,
        )
    }
}