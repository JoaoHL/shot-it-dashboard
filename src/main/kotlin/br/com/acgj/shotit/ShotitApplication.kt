package br.com.acgj.shotit

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
@EnableScheduling
class ShotitApplication(val manager: EntityManager) : WebMvcConfigurer, CommandLineRunner {

	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*")
	}

	@Transactional
	override fun run(vararg args: String?) {
//		mutableListOf<String>(
//			"kotlin",
//			"java",
//			"tech-challenge",
//		)
//		.map { VideoCategoryTag(name =  it ) }
//		.forEach(manager::persist)
	}
}

fun main(args: Array<String>) {
	runApplication<ShotitApplication>(*args)
}
