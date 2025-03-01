package br.com.acgj.shotit.core.videos.gateways

import br.com.acgj.shotit.core.domain.Video

interface VideoPreSignGateway {
    suspend fun sign(video: Video): SignedVideo
}