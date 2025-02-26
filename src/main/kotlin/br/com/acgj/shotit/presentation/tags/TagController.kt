package br.com.acgj.shotit.presentation.tags

import br.com.acgj.shotit.infra.VideoCategoryTagRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class TagDTO(val id: Long, val name: String)

@RestController
@RequestMapping("/api/tags")
class TagController(private val repository: VideoCategoryTagRepository) {

    @GetMapping
    fun handleRetrieveTags() = ResponseEntity.ok( repository.findAll().map { TagDTO(it.id!!, it.name) } )
}