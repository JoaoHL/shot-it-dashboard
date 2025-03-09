package br.com.acgj.shotit.core.videos.gateways

import br.com.acgj.shotit.core.domain.Thumbnail

interface ThumbnailDownloadGateway {
    suspend fun retrieve(thumbnails: List<Thumbnail>): ByteArray
}
