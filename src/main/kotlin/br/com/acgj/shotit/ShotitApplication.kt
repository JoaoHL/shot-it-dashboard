package br.com.acgj.shotit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
class ShotitApplication : WebMvcConfigurer{

	override fun addCorsMappings(registry: CorsRegistry) {
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*")
	}
}

fun main(args: Array<String>) {
	runApplication<ShotitApplication>(*args)
}
