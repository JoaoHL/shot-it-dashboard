package br.com.acgj.shotit.core.infra.cloud

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.ses.SesClient
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
) {
    @Bean
    fun s3Client(): S3Client  =  S3Client {
        region = this@AwsConfiguration.region
        endpointUrl = Url.parse(this@AwsConfiguration.endpoint)
        forcePathStyle = true
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = accessKey
            secretAccessKey = secretKey
        }
    }

    @Bean
    fun sesClient(): SesClient =
        SesClient {
            region = this@AwsConfiguration.region
            endpointUrl = Url.parse(this@AwsConfiguration.endpoint)
            credentialsProvider = StaticCredentialsProvider {
                accessKeyId = accessKey
                secretAccessKey = secretKey
            }
        }
}
