package br.com.acgj.shotit.core.videos.services

import br.com.acgj.shotit.core.domain.BadRequestError
import br.com.acgj.shotit.core.domain.VideoRepository
import br.com.acgj.shotit.core.videos.events.UploadVideoProducer
import org.springframework.stereotype.Service

@Service
class VideoService(private val repository: VideoRepository, private val producer: UploadVideoProducer){
    fun sendUploadedVideoToQueue(id: Long){
        val video = repository
            .findById(id)
            .orElseThrow { BadRequestError("Invalid Video", "invalid_video", "Video not found") }

        producer.createdVideo(video)
    }
}