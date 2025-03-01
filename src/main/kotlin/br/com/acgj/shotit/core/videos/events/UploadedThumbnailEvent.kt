package br.com.acgj.shotit.core.videos.events

import com.fasterxml.jackson.annotation.JsonProperty

data class UploadedThumbnailEvent(
    @JsonProperty("video_id") val video : Long,
    @JsonProperty("urls") val urls: List<String>
)
