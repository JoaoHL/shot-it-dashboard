package br.com.acgj.shotit

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class ShotitApplication {

	@Bean
	fun runner(template: AmqpTemplate): ApplicationRunner {
		return ApplicationRunner { args: ApplicationArguments? -> template.convertAndSend("myqueue", "foo") }
	}

	@Bean
	fun myQueue(): Queue {
		return Queue("myqueue")
	}
}

fun main(args: Array<String>) {
	runApplication<ShotitApplication>(*args)
}
