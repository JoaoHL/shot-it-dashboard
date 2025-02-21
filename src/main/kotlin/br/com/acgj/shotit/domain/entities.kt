package br.com.acgj.shotit.domain

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import kotlin.jvm.Transient

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    var username: String,

    var name: String,

    var email: String,

    var password: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val videos: Set<Video> = setOf()
)

@Entity
@Table(name = "videos")
data class Video(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne @JoinColumn(name = "user_id")
    var user: User? = null,

    @Enumerated(STRING)
    var status: VideoStatus = VideoStatus.PENDING,

    var name: String,

    var url: String? = null,

    @Transient
    var file: MultipartFile? = null,

    @Column(name = "thumbnail_url")
    var thumbnailUrl: String? = null,
)

enum class VideoStatus {
    PENDING,
    SUCCESS,
    FAILED
}
