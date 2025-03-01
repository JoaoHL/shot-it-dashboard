package br.com.acgj.shotit.core.videos.controllers

import br.com.acgj.shotit.core.videos.services.UploadVideoService
import br.com.acgj.shotit.core.domain.BadRequestError
import br.com.acgj.shotit.core.domain.NotFoundError
import br.com.acgj.shotit.core.domain.VideoRepository
import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.videos.gateways.S3VideoPreSignGateway
import br.com.acgj.shotit.core.videos.ports.UpdateThumbnailFavorite
import br.com.acgj.shotit.core.videos.ports.UpdateVideoTags
import br.com.acgj.shotit.core.videos.ports.UpdateVideoTitleRequest
import br.com.acgj.shotit.core.videos.ports.VideoDTO
import br.com.acgj.shotit.core.videos.services.EditVideoService
import br.com.acgj.shotit.core.videos.services.RetrieveVideoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/videos")
class VideoController(
    private val applicationUser: ApplicationUser,
    private val retrieveVideoService: RetrieveVideoService,
    private val videoRepository: VideoRepository,
    private val uploadVideoService: UploadVideoService,
    private val editVideoService: EditVideoService,
    private val preSignGateway: S3VideoPreSignGateway
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

    @PatchMapping("/{id}/thumbnails/favorite")
    fun handleFavoriteThumbnail(@PathVariable("id") id: Long, @RequestBody request: UpdateThumbnailFavorite){
        editVideoService.changeThumbnail(request.thumbnailId)
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
