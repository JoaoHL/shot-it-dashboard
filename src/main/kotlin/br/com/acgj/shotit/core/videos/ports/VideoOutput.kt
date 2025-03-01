package br.com.acgj.shotit.core.videos.ports

data class SignedVideoDTO(val id: Long, val url: String, val filename: String)

data class VideoThumbnail(
    val id: Long,
    val url: String
)

data class VideoTags(val id: Long, val name: String)

data class VideoDTO(
    val id: Long,
    val name: String,
    val url: String? = null,
    val thumbnails: List<VideoThumbnail>,
    val tags: List<VideoTags>
)