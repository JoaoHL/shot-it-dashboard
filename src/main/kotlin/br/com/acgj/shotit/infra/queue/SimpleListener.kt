package br.com.acgj.shotit.infra.queue

import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class SimpleListener {

    @RabbitListener(queues = [ "user.queue" ])
    fun handleListen(message: Message){
        TODO("Implement ")
    }
}
