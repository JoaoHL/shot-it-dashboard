package br.com.acgj.shotit.infra.upload

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.asByteStream
import aws.smithy.kotlin.runtime.content.writeToOutputStream
import aws.smithy.kotlin.runtime.net.url.Url
import br.com.acgj.shotit.domain.Video
import br.com.acgj.shotit.utils.normalizeFilename
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream

class S3UploadGateway(
    private val client: S3Client,
    private val logger: Logger = LoggerFactory.getLogger("S3Uploader")
) : UploadGateway {

    private final val BUCKET: String = "shotit"

    override suspend fun upload(video: Video): String {
        val file = video.file!!
        val fileKey = normalizeFilename(file.originalFilename!!)

        val request = PutObjectRequest {
            bucket = BUCKET
            key = fileKey
            body = file.inputStream.asByteStream()
            contentType = "video/mp4"
            contentEncoding = "UTF-8"
        }

        S3Client {
            region = "us-east-1"
            endpointUrl = Url.parse("http://localhost:4566")
            forcePathStyle = true
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = "local"
                secretAccessKey = "stack"
            }
        }.use {
            run {
                try {
                    it.putObject(request)
                } catch (e: Exception) {
                    println(e)
                }

            }
        }

        logger.info("S3: Finished Upload for ${file.originalFilename!!}")

        return fileKey
    }

    override suspend fun retrieve(video: String): ByteArrayOutputStream {
        return S3Client {
            region = "us-east-1"
            endpointUrl = Url.parse("http://localhost:4566")
            forcePathStyle = true
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = "local"
                secretAccessKey = "stack"
            }
        }.use { s3 ->
            s3.getObject(
                GetObjectRequest {
                    bucket = BUCKET
                    key = video
                }
            ) {
                content ->
                val output = ByteArrayOutputStream()
                content.body?.writeToOutputStream(output)

                output
            }
        }




    }

}
