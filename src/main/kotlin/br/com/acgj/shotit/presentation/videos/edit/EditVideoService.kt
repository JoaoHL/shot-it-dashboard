package br.com.acgj.shotit.presentation.videos.edit

import br.com.acgj.shotit.domain.User
import br.com.acgj.shotit.infra.VideoRepository
import org.springframework.stereotype.Service

@Service
class EditVideoService(private val videoRepository: VideoRepository) {

    fun updateVideoTitle(user: User, id: Long, name: String): Boolean{
        val rows = videoRepository.updateVideoTitle(name, id, user)
        return rows > 0;
    }

}