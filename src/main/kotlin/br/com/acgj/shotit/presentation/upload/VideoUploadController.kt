package br.com.acgj.shotit.presentation.upload

import br.com.acgj.shotit.application.UploadVideoService
import br.com.acgj.shotit.domain.BadRequestError
import br.com.acgj.shotit.infra.security.ApplicationUser
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/videos")
class VideoUploadController(private val applicationUser: ApplicationUser, private val service: UploadVideoService) {

    @PostMapping
    fun handleUploadVideos(@Valid request: UploadVideoRequest) : ResponseEntity<Void> {
        val user = applicationUser.fromContext()
            ?: throw BadRequestError("Authentication Error", "unlogged_user", "User must be logged")

        val videos = request.toDomain(user)

        service.upload(videos)

        return ResponseEntity.ok().build<Void>()
    }


}