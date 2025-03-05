package br.com.acgj.shotit.core.scheduler

import br.com.acgj.shotit.core.domain.VideoRepository
import br.com.acgj.shotit.core.domain.VideoStatus
import br.com.acgj.shotit.core.domain.VideoStatus.PENDING
import br.com.acgj.shotit.core.videos.events.UploadVideoProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SendPendingVideosToQueue(
    val repository: VideoRepository,
    val producer: UploadVideoProducer,
    val logger: Logger = LoggerFactory.getLogger(SendPendingVideosToQueue::class.java)
) {
    @Scheduled(cron = "@hourly")
    fun handleSendToQueue(){
        logger.info("${LocalDateTime.now()}: Starting sending pending videos to queue")
        repository.findAllByStatus(PENDING).forEach { producer.createdVideo(it) }
    }
}