package br.com.acgj.shotit.application

import br.com.acgj.shotit.domain.Video

interface UploadVideoService {
    fun upload(videos: List<Video>)
}
