package br.com.acgj.shotit.infra.configuration

import br.com.acgj.shotit.infra.upload.LocalUploadVideoGateway
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
    ): UploadGateway = LocalUploadVideoGateway(loader = loader, folderName = folderName)

}
