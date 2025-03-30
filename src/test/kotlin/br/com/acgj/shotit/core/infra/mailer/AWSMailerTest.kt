package br.com.acgj.shotit.core.infra.mailer

import br.com.acgj.shotit.InfraContainersForTestConfiguration
import br.com.acgj.shotit.core.domain.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AWSMailerTest : InfraContainersForTestConfiguration() {

    @Autowired
    private lateinit var awsMailer: AWSMailer

    @Test
    fun `should send email successfully`() = runBlocking {
        verifyEmail()
        val user = User(
            username = "testuser",
            name = "Test User",
            email = "test@example.com",
            password = "password123"
        )

        val emailTemplate = object : EmailTemplate {
            override val subject: String = "Test Subject"
            override fun body(): String = "<h1>Test Body</h1>"
        }

        awsMailer.send(user, emailTemplate)
    }
} 