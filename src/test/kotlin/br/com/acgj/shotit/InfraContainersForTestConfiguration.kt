package br.com.acgj.shotit

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class InfraContainersForTestConfiguration {
    companion object {
        @Container
        private val mysql = MySQLContainer("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")

        @Container
        private val rabbitmq = RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExchange("video.event", "topic")
            .withQueue("video.processed")
            .withQueue("video.thumbnail")
            .withBinding("video.event", "video.processed", mapOf(), "video.upload", "topic")
            .withBinding("video.event", "video.thumbnail", mapOf(), "thumbnail.generated", "topic")

        @Container
        private val localstack = LocalStackContainer(DockerImageName.parse("localstack/localstack:2.1"))
            .withServices(LocalStackContainer.Service.S3)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            // MySQL properties
            registry.add("spring.datasource.url", mysql::getJdbcUrl)
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)

            // RabbitMQ properties
            registry.add("spring.rabbitmq.host") { rabbitmq.host }
            registry.add("spring.rabbitmq.port") { rabbitmq.amqpPort }
            registry.add("spring.rabbitmq.username") { rabbitmq.adminUsername }
            registry.add("spring.rabbitmq.password") { rabbitmq.adminPassword }

            // LocalStack properties
            registry.add("aws.endpoint") { localstack.getEndpoint() }
            registry.add("aws.region") { localstack.region }
            registry.add("aws.credentials.access-key") { localstack.accessKey }
            registry.add("aws.credentials.secret-key") { localstack.secretKey }
        }
    }
} 