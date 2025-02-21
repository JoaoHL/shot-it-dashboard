package br.com.acgj.shotit.infra.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class AwsConfiguration(
    @Value("\${aws.accessKey}") val accessKey: String,
    @Value("\${aws.secretKey") val secretKey: String,
    @Value("\${aws.region}") val region: String
) {

    @Bean
    fun handleCreateAWSCredentials() = AwsBasicCredentials.create(accessKey, secretKey)

    @Bean
    fun handleCreateS3Client(@Value("\${aws.url}") url: String) = S3Client
        .builder()
        .endpointOverride(URI.create(url))
        .region(Region.of(region))
        .credentialsProvider(this::handleCreateAWSCredentials)
        .build()
}
