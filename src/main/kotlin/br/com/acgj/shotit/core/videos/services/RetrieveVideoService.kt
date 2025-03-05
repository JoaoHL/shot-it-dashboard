package br.com.acgj.shotit.core.videos.services

import br.com.acgj.shotit.core.domain.User
import br.com.acgj.shotit.core.domain.VideoRepository
import br.com.acgj.shotit.core.videos.ports.VideoDTO
import br.com.acgj.shotit.core.videos.ports.VideoThumbnail
import br.com.acgj.shotit.core.videos.ports.VideoTags
import org.springframework.stereotype.Service
import java.util.*

@Service
class RetrieveVideoService(private val videoRepository: VideoRepository){

    fun retrieve(user: User): List<VideoDTO>? {
        return videoRepository.findByUser(user).map {
            VideoDTO(
                video = it,
                tags = videoRepository.retrieveForVideo(it)
            )
        }
    }

    fun findById(id: Long): Optional<VideoDTO> {
        return videoRepository.eagerFindById(id).map {
            VideoDTO(
                video = it,
                tags = videoRepository.retrieveForVideo(it)
            )
        }
    }

}
