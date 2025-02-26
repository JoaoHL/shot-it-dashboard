package br.com.acgj.shotit.presentation.videos

import br.com.acgj.shotit.application.UploadVideoService
import br.com.acgj.shotit.domain.BadRequestError
import br.com.acgj.shotit.domain.NotFoundError
import br.com.acgj.shotit.infra.security.ApplicationUser
import br.com.acgj.shotit.presentation.videos.edit.EditVideoService
import br.com.acgj.shotit.presentation.videos.edit.UpdateVideoTags
import br.com.acgj.shotit.presentation.videos.edit.UpdateVideoTitleRequest
import br.com.acgj.shotit.presentation.videos.retrieve.RetrieveVideoService
import br.com.acgj.shotit.presentation.videos.retrieve.VideoDTO
import br.com.acgj.shotit.presentation.videos.upload.UploadVideoRequest
import jakarta.validation.Valid
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/videos")
class VideoController(
    private val applicationUser: ApplicationUser,
    private val retrieveVideoService: RetrieveVideoService,
    private val uploadVideoService: UploadVideoService,
    private val editVideoService: EditVideoService
){

    @GetMapping
    fun handleRetrieveUserVideos(): ResponseEntity<Any> {
        val user = applicationUser.fromContext() ?: throw NotFoundError("User not found")
        val videos = retrieveVideoService.retrieve(user)
        return ResponseEntity.ok().body(videos)
    }

    @PostMapping
    fun handleUploadVideos(@Valid request: UploadVideoRequest): ResponseEntity<Void> {
        val user = applicationUser.fromContext()
            ?: throw BadRequestError("Authentication Error", "unlogged_user", "User must be logged")

        val videos = request.toDomain(user)

        runBlocking {
            uploadVideoService.upload(videos)
        }

        return ResponseEntity.ok().build<Void>()
    }

    @GetMapping("/{id}")
    fun handleRetrieveVideo(@PathVariable("id") id: Long): ResponseEntity<VideoDTO>? {
        val video = retrieveVideoService.findById(id);

        return video
            .map { ResponseEntity.ok(it) }
            .orElseThrow{ NotFoundError("Video not found") }
    }

    @PatchMapping("/{id}/change-name")
    fun handleChangeVideoName(@PathVariable("id") id: Long, @RequestBody request: UpdateVideoTitleRequest): ResponseEntity<Any> {
        val user = applicationUser.fromContext()
            ?: throw BadRequestError("Authentication Error", "unlogged_user", "User must be logged")

        val updated = editVideoService.updateVideoTitle(user, id, request.title)

        if (!updated){
            throw BadRequestError("Invalid Request", "video_not_found", "There is no video for this user")
        }

        return ResponseEntity.ok().build<Any>()
    }

    @PatchMapping("/{id}/tags")
    fun handleChangeVideoTags(@PathVariable("id") id: Long, @RequestBody request: UpdateVideoTags): ResponseEntity<Any> {
        editVideoService.updateVideoTags(id, request.tagIds)

        return ResponseEntity.ok().build()
    }
}
