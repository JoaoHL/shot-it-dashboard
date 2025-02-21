package br.com.acgj.shotit.infra.upload

import br.com.acgj.shotit.domain.Video

interface UploadGateway {
    fun upload(video: Video) : String
}