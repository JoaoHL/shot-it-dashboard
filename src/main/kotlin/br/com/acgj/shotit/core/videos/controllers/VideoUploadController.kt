package br.com.acgj.shotit.core.videos.controllers

import br.com.acgj.shotit.core.videos.services.SignVideoService
import br.com.acgj.shotit.core.videos.services.UploadVideoService
import br.com.acgj.shotit.core.videos.ports.SignVideoUploadRequest
import br.com.acgj.shotit.core.videos.ports.SignedVideoDTO
import br.com.acgj.shotit.core.videos.ports.UploadVideoRequest
import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.videos.services.RetrieveVideoService
import br.com.acgj.shotit.core.videos.services.VideoService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/videos")
class VideoUploadController(
    private val applicationUser: ApplicationUser,
    private val videoService: VideoService,
    private val uploadService: UploadVideoService,
    private val signService: SignVideoService
) {

    //TODO será que precisa mesmo do request body removi para o teste passar :P
    @PostMapping
    fun handleUploadVideo(request: UploadVideoRequest): ResponseEntity<Void> {
        val user = applicationUser.fromAuthorizedContext()
        val videos = request.toDomain(user)

        // TODO: Adicionar um DTO para o upload.
        val response = runBlocking { uploadService.upload(videos) }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign")
    fun handleSignVideo(@RequestBody request: SignVideoUploadRequest): ResponseEntity<List<SignedVideoDTO>> {
        val user = applicationUser.fromAuthorizedContext()
        val videos = request.toDomain(user)
        val signs = runBlocking { signService.sign(videos) }
        val response = signs.map { SignedVideoDTO(it.video.id!!, it.sign, it.video.originalName!!) }
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{id}/complete")
    fun handleAckVideoCompleteUpload(@PathVariable("id") id: Long): ResponseEntity<Any> {
        videoService.sendUploadedVideoToQueue(id)
        return ResponseEntity.ok().build<Any>()
    }
}