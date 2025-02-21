package br.com.acgj.shotit.presentation.retrieve

import br.com.acgj.shotit.infra.security.ApplicationUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ping")
class PingController(val applicationUser: ApplicationUser) {
    @GetMapping
    fun ping(): ResponseEntity<String> = ResponseEntity.ok().body("Pong!")
}