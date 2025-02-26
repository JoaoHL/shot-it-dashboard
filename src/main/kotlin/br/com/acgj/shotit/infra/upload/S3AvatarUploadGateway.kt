package br.com.acgj.shotit.infra.upload

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.asByteStream
import br.com.acgj.shotit.utils.normalizeFilename
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class S3AvatarUploadGateway(
    private val client: S3Client
){

    companion object {
        const val BUCKET_NAME = "shotit"
    }

    suspend fun upload(username: String, image: MultipartFile): String {
        val normalized = normalizeFilename(image.originalFilename!!)
        val fileKey = "${username}_${normalized}"

        val request = PutObjectRequest {
            bucket = BUCKET_NAME
            key = fileKey
            body = image.inputStream.asByteStream()
            contentType = image.contentType
            contentEncoding = "UTF-8"
        }

        client.use { it.putObject(request) }

        val endpoint = client.config.endpointUrl.toString()

        return "${endpoint}/$BUCKET_NAME/${fileKey}"
    }
}
