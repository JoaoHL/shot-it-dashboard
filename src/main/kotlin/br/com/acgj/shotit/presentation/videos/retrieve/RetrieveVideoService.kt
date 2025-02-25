package br.com.acgj.shotit.presentation.videos.retrieve

import br.com.acgj.shotit.domain.User
import br.com.acgj.shotit.infra.VideoRepository
import org.springframework.stereotype.Service
import java.util.*

data class VideoThumbnail(
    val id: Long,
    val url: String
)

data class VideoDTO(
    val id: Long,
    val name: String,
    val url: String,
    val thumbnails: List<VideoThumbnail>
)

@Service
class RetrieveVideoService(private val videoRepository: VideoRepository){
    fun retrieve(user: User): List<VideoDTO>? {
        return videoRepository.findByUser(user).map {
            VideoDTO(
                id = it.id!!,
                name = it.name,
                url = it.url!!,
            thumbnails = it.thumbnails?.map { thumbnail -> VideoThumbnail(thumbnail.id!!, thumbnail.url) } ?: emptyList()
            )
        }
    }

    fun findById(id: Long): Optional<VideoDTO> {
        return videoRepository.eagerFindById(id).map {
            VideoDTO(
                id = it.id!!,
                name = it.name,
                url = it.url!!,
                thumbnails = it.thumbnails?.map { thumbnail -> VideoThumbnail(thumbnail.id!!, thumbnail.url) } ?: emptyList()
            )
        }
    }

}
