package br.com.acgj.shotit.presentation.videos.edit

data class UpdateVideoTitleRequest(val title: String)

data class UpdateVideoTags(val tagIds: MutableList<Long>)