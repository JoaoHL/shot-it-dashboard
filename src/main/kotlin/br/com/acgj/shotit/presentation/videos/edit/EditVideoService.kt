package br.com.acgj.shotit.presentation.videos.edit

import br.com.acgj.shotit.domain.NotFoundError
import br.com.acgj.shotit.domain.User
import br.com.acgj.shotit.infra.VideoCategoryTagRepository
import br.com.acgj.shotit.infra.VideoRepository
import org.springframework.stereotype.Service

@Service
class EditVideoService(
    private val videoRepository: VideoRepository,
    private val tagsRepository: VideoCategoryTagRepository
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

}