package br.com.acgj.shotit.core.videos.controllers

import br.com.acgj.shotit.core.domain.NotFoundError
import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.videos.ports.VideoDTO
import br.com.acgj.shotit.core.videos.services.EditVideoService
import br.com.acgj.shotit.core.videos.services.RetrieveVideoService
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/videos")
class VideoController(
    private val applicationUser: ApplicationUser,
    private val retrieveVideoService: RetrieveVideoService,
    private val editVideoService: EditVideoService
){

    @GetMapping
    fun handleRetrieveUserVideos(): ResponseEntity<Any> {
        val user = applicationUser.fromAuthorizedContext()
        val videos = retrieveVideoService.retrieve(user)
        return ResponseEntity.ok().body(videos)
    }

    @GetMapping("/{id}")
    fun handleRetrieveVideo(@PathVariable("id") id: Long): ResponseEntity<VideoDTO>? {
        val video = retrieveVideoService.findById(id);

        return video
            .map { ResponseEntity.ok(it) }
            .orElseThrow{ NotFoundError("Video not found") }
    }

    @GetMapping("/{id}/thumbnails/download")
    fun handleDownloadThumbnails(@PathVariable("id") id: Long): ResponseEntity<ByteArray> {
        val zip = runBlocking { retrieveVideoService.downloadThumbnails(id) }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
            set("Content-Disposition", "attachment; filename=\"images.zip\"")
        }

        return ResponseEntity.ok().headers(headers).body(zip)
    }
}
