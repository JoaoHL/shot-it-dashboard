package br.com.acgj.shotit.core.videosTags.controllers

import br.com.acgj.shotit.core.domain.VideoCategoryTagRepository
import br.com.acgj.shotit.core.videosTags.ports.TagDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class VideoTagController(private val repository: VideoCategoryTagRepository) {
    @GetMapping
    fun handleRetrieveTags() = ResponseEntity.ok( repository.findAll().map { TagDTO(it.id!!, it.name) } )
}