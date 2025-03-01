package br.com.acgj.shotit.core.videos.services

import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.core.domain.VideoRepository
import br.com.acgj.shotit.core.videos.gateways.SignedVideo
import br.com.acgj.shotit.core.videos.gateways.VideoPreSignGateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class SignVideoServiceImpl(
    private val repository: VideoRepository,
    private val gateway: VideoPreSignGateway
) : SignVideoService {

    override suspend fun sign(videos: List<Video>): List<SignedVideo> {
        val signs = videos.map {
            withContext(Dispatchers.IO){
                val sign = gateway.sign(it)
                it.url = sign.url

                sign
            }
        }

        repository.saveAll(signs.map { it.video } )

        return signs
    }


}