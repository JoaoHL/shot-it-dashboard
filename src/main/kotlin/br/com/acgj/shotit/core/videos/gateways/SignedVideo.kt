package br.com.acgj.shotit.core.videos.gateways

import br.com.acgj.shotit.core.domain.Video

data class SignedVideo(val video: Video, val url: String, val sign: String)