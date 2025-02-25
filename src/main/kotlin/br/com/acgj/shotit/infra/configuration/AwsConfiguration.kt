package br.com.acgj.shotit.infra.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfiguration(
    @Value("\${aws.accessKey}") val accessKey: String,
    @Value("\${aws.secretKey") val secretKey: String,
    @Value("\${aws.region}") val region: String
)
