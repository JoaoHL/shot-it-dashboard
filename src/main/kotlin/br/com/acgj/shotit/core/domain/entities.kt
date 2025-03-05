package br.com.acgj.shotit.core.domain

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

    var profilePicture: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val videos: MutableList<Video>? = null,
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

    @OneToMany(mappedBy = "video", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, targetEntity = Thumbnail::class)
    var thumbnails: MutableList<Thumbnail>? = null,

    @ManyToMany @JoinTable(
        name = "video_tags",
        joinColumns = [JoinColumn(name = "video_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableList<VideoCategoryTag>?= null,

    @Enumerated(STRING)
    var status: VideoStatus = VideoStatus.PENDING,

    var name: String,

    var url: String? = null,

    @Transient
    var file: MultipartFile? = null,

    @Transient
    var originalName: String? = null
)

@Entity
@Table(name = "thumbnails")
data class Thumbnail(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne @JoinColumn(name = "video_id")
    var video: Video? = null,

    var url: String,

    var principal: Boolean = false
)

@Entity
@Table(name = "tags")
data class VideoCategoryTag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToMany(mappedBy = "tags")
    var video: MutableList<Video>? = null,

    var name: String,
)

enum class VideoStatus {
    PENDING,
    UPLOADED,
    SUCCESS,
    FAILED
}

