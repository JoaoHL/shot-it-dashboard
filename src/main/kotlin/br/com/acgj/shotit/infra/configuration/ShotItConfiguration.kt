package br.com.acgj.shotit.infra.configuration

import aws.sdk.kotlin.services.s3.S3Client
import br.com.acgj.shotit.infra.upload.S3UploadGateway
import br.com.acgj.shotit.infra.upload.UploadGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class ShotItConfiguration {

    @Bean
    fun uploadGateway(
        loader: ResourceLoader,
        @Value("\${storage.local}") folderName: String
    ): UploadGateway = S3UploadGateway(client = S3Client {})

}
