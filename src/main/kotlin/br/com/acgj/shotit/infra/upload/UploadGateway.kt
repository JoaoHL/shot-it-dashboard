package br.com.acgj.shotit.infra.upload

import br.com.acgj.shotit.domain.Video
import java.io.ByteArrayOutputStream

interface UploadGateway {
    suspend fun upload(video: Video) : String
    suspend fun retrieve(key: String): ByteArrayOutputStream
}