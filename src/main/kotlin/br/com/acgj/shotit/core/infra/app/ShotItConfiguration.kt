package br.com.acgj.shotit.core.infra.app

import aws.sdk.kotlin.services.s3.S3Client
import br.com.acgj.shotit.core.videos.gateways.S3VideoUploadGateway
import br.com.acgj.shotit.core.videos.gateways.VideoUploadGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class ShotItConfiguration {

    @Bean
    fun uploadGateway(
        s3Client: S3Client,
        loader: ResourceLoader,
        @Value("\${storage.local}") folderName: String
    ): VideoUploadGateway = S3VideoUploadGateway(client = s3Client )

}
