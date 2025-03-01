package br.com.acgj.shotit.core.videos.services

import br.com.acgj.shotit.core.domain.NotFoundError
import br.com.acgj.shotit.core.domain.User
import br.com.acgj.shotit.core.domain.ThumbnailRepository
import br.com.acgj.shotit.core.domain.VideoCategoryTagRepository
import br.com.acgj.shotit.core.domain.VideoRepository
import org.springframework.stereotype.Service

@Service
class EditVideoService(
    private val videoRepository: VideoRepository,
    private val tagsRepository: VideoCategoryTagRepository,
    private val thumbnailRepository: ThumbnailRepository
) {

    fun updateVideoTitle(user: User, id: Long, name: String): Boolean{
        val rows = videoRepository.updateVideoTitle(name, id, user)
        return rows > 0;
    }

    fun updateVideoTags(videoId: Long, tags: MutableList<Long>){
        val video = videoRepository.findById(videoId).orElseThrow { NotFoundError("Video not found") }
        val tags = tagsRepository.findAllById(tags);

        video.tags = tags

        videoRepository.save(video)
    }

    fun changeThumbnail(thumbnailId: Long){
        val thumbnail = thumbnailRepository.findById(thumbnailId).orElseThrow { NotFoundError("Thumbnail not found") }

        thumbnailRepository.findCurrentFavorite(thumbnail.video!!).ifPresent { current ->
            current.principal = false
            thumbnailRepository.save(current)
        }

        thumbnail.principal = true
        thumbnailRepository.save(thumbnail)
    }

}