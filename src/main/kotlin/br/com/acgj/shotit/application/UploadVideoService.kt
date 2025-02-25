package br.com.acgj.shotit.application

import br.com.acgj.shotit.domain.Video
import java.io.ByteArrayOutputStream

interface UploadVideoService {
    suspend fun upload(videos: List<Video>)
    suspend fun retrieve(key: String): ByteArrayOutputStream
}
