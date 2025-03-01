package br.com.acgj.shotit.core.videos.services

import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.core.videos.gateways.SignedVideo

interface SignVideoService {
    suspend fun sign(videos: List<Video>) : List<SignedVideo>
}