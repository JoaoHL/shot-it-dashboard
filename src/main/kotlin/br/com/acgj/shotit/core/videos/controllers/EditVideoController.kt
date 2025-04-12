package br.com.acgj.shotit.core.videos.controllers

import br.com.acgj.shotit.core.domain.BadRequestError
import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.videos.ports.UpdateThumbnailFavorite
import br.com.acgj.shotit.core.videos.ports.UpdateVideoTags
import br.com.acgj.shotit.core.videos.ports.UpdateVideoTitleRequest
import br.com.acgj.shotit.core.videos.services.EditVideoService
import br.com.acgj.shotit.core.videos.services.RetrieveVideoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/videos")
class EditVideoController(
    private val applicationUser: ApplicationUser,
    private val retrieveVideoService: RetrieveVideoService,
    private val editVideoService: EditVideoService
){
    @PatchMapping("/{id}/thumbnails/favorite")
    fun handleFavoriteThumbnail(@PathVariable("id") id: Long, @RequestBody request: UpdateThumbnailFavorite){
        editVideoService.changeThumbnail(request.thumbnailId)
    }

    @PatchMapping("/{id}/change-name")
    fun handleChangeVideoName(@PathVariable("id") id: Long, @RequestBody request: UpdateVideoTitleRequest): ResponseEntity<Any> {
        val user = applicationUser.fromContext()!!

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
