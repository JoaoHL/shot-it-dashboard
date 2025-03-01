package br.com.acgj.shotit.core.users.controllers

import br.com.acgj.shotit.core.domain.Thumbnail
import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.auth.gateways.S3AvatarUploadGateway
import br.com.acgj.shotit.core.users.ports.UserProfileDTO
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profile")
class UserProfileController(private val applicationUser: ApplicationUser, private val gateway: S3AvatarUploadGateway) {

    @GetMapping
    fun handleGetProfile(): ResponseEntity<UserProfileDTO> = applicationUser
        .fromContext()
        ?.let { ResponseEntity.ok(UserProfileDTO(it)) }
        ?: ResponseEntity.notFound().build();

    @GetMapping("/zip")
    fun example(): ResponseEntity<ByteArray> {
        val items = listOf(
            Thumbnail(13L, null,"", false),
            Thumbnail(14L, null,"", false),
            Thumbnail(15L, null,"", false),
        )

        val output = runBlocking { gateway.retrieve(items)  }


        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
            set("Content-Disposition", "attachment; filename=\"images.zip\"")
        }

        return ResponseEntity.ok().headers(headers).body(output)
    }

}