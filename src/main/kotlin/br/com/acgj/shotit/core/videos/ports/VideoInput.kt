package br.com.acgj.shotit.core.videos.ports

import br.com.acgj.shotit.core.domain.User
import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.utils.normalizeFilename
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartFile

data class UpdateVideoTitleRequest(val title: String)
data class UpdateThumbnailFavorite(val thumbnailId: Long)
data class UpdateVideoTags(val tagIds: MutableList<Long>)

data class SignVideoUploadRequest(@NotNull @Size(min = 1, max = 5) val names: List<String>){
    fun toDomain(user: User): List<Video> {
        return names.map {
            Video (
                name = normalizeFilename(it),
                user = user,
                originalName = it
            )
        }
    }
}

data class UploadVideoRequest(@NotNull @Size(min = 1, max = 5) val files: Set<MultipartFile>) {
    fun toDomain(user: User): List<Video> {
        return files.stream().map {
            Video(
                name = normalizeFilename(it.name),
                file = it,
                user = user
            )
        }
            .toList()
    }
}