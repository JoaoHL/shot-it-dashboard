package br.com.acgj.shotit.core.infra.cloud

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.net.url.Url
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfiguration(
    @Value("\${aws.accessKey}") val accessKey: String,
    @Value("\${aws.secretKey}") val secretKey: String,
    @Value("\${aws.region}") val region: String,
    @Value("\${aws.endpoint}") val endpoint: String
){
    @Bean
    fun s3Client(): S3Client {
        val builder = S3Client.builder()

        builder.config.region = region
        builder.config.endpointUrl = Url.parse(endpoint)
        builder.config.forcePathStyle = true
        builder.config.credentialsProvider = StaticCredentialsProvider {
            accessKeyId = accessKey
            secretAccessKey = secretKey
        }

        return builder.build()
    }
}
