package br.com.acgj.shotit.presentation.upload

import br.com.acgj.shotit.domain.User
import br.com.acgj.shotit.domain.Video
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartFile

data class UploadVideoRequest(@NotNull @Size(min = 1, max = 5) val files: Set<MultipartFile>) {

    fun toDomain(user: User): List<Video> {
        return files.stream().map {
            Video(
                name = it.name,
                file = it,
                user = user
            )
        }
            .toList()
    }

}