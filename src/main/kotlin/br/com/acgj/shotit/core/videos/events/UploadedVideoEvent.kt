package br.com.acgj.shotit.core.videos.events

import java.io.Serializable

data class UploadedVideoEvent(
    val id: Long,
    val url: String,
    val timestamp: String
) : Serializable

