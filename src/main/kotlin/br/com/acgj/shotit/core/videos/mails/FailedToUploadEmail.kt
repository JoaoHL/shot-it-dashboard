package br.com.acgj.shotit.core.videos.mails

import br.com.acgj.shotit.core.domain.Video
import br.com.acgj.shotit.core.infra.mailer.EmailTemplate
import br.com.acgj.shotit.core.infra.mailer.email

data class FailedToUploadEmail(private val video: Video) : EmailTemplate {
    override val subject = "ShotIt: Falha no Envio"

    override fun body(): String = email {
        title ("ShotIt: Video ${video.name}")
        body {
            p ("Caro Usuário, ")
            p ("Houve um problema ao gerar seu vídeo com título ${video.name}")
            p ("Pedimos que tente realizar o upload novamente em nossa plataforma")
        }
        callToAction {
            message("Acesse suas dashboard:")
            link("http://localhost:8080/dashboard", "Ver dashboard")
        }
    }
}