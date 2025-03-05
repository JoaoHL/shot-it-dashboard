package br.com.acgj.shotit.core.videos.ports

import br.com.acgj.shotit.core.domain.Thumbnail
import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.core.domain.VideoCategoryTag

data class SignedVideoDTO(val id: Long, val url: String, val filename: String)

data class VideoThumbnail(
    val id: Long,
    val url: String
){
    constructor(thumbnail: Thumbnail) : this(thumbnail.id!!, thumbnail.url)
}

data class VideoTags(val id: Long, val name: String){
    constructor(tag: VideoCategoryTag): this(tag.id!!, tag.name)
}

data class VideoDTO(
    val id: Long,
    val name: String,
    val status: String,
    val url: String? = null,
    val thumbnails: List<VideoThumbnail>,
    val tags: List<VideoTags>
){
    constructor(video: Video, tags: List<VideoCategoryTag>) : this(
        video.id!!,
        video.name,
        video.status.name,
        video.url,
        video.thumbnails.orEmpty().map { VideoThumbnail(it) },
        tags.map { VideoTags(it) }
    )
}