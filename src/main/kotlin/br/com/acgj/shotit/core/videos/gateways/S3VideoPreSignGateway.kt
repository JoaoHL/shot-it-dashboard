package br.com.acgj.shotit.core.videos.gateways

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignPutObject
import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.core.domain.VideoRepository
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.hours

@Service
class S3VideoPreSignGateway(
    private val videoRepository: VideoRepository,
    private val s3Client: S3Client
) : VideoPreSignGateway {

    companion object {
        const val BUCKET_NAME = "shotit"
    }

    override suspend fun sign(video: Video): SignedVideo {
        val request = PutObjectRequest {
            bucket = BUCKET_NAME
            key = video.name
        }

        val response = s3Client.presignPutObject(request, 1.hours)

        val endpoint = s3Client.config.endpointUrl.toString()

        val url = "${endpoint}/$BUCKET_NAME/${video.name}"

        return SignedVideo(video, url, response.url.toString())
    }

}