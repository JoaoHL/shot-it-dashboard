package br.com.acgj.shotit.core.users.controllers

import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.auth.gateways.S3AvatarUploadGateway
import br.com.acgj.shotit.core.users.ports.UserProfileDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profile")
class UserProfileController(private val applicationUser: ApplicationUser, private val gateway: S3AvatarUploadGateway) {

    @GetMapping
    fun handleGetProfile(): ResponseEntity<UserProfileDTO> {
        val fromContext = applicationUser
            .fromContext()
        return   ResponseEntity.ok(UserProfileDTO(fromContext!!))
    }
}