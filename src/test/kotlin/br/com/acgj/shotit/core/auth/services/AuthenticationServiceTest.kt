package br.com.acgj.shotit.core.auth.services

import br.com.acgj.shotit.core.auth.gateways.S3AvatarUploadGateway
import br.com.acgj.shotit.core.auth.ports.SignInRequest
import br.com.acgj.shotit.core.auth.ports.SignUpRequest
import br.com.acgj.shotit.core.domain.BadRequestError
import br.com.acgj.shotit.core.domain.User
import br.com.acgj.shotit.core.domain.UserRepository
import br.com.acgj.shotit.core.infra.auth.JWTService
import br.com.acgj.shotit.core.infra.auth.UserDetailsImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthenticationServiceTest {
    private lateinit var authenticationService: AuthenticationService
    private lateinit var manager: AuthenticationManager
    private lateinit var userRepository: UserRepository
    private lateinit var uploadService: S3AvatarUploadGateway
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtService: JWTService

    @BeforeEach
    fun setup() {
        manager = mockk()
        userRepository = mockk()
        uploadService = mockk()
        passwordEncoder = mockk()
        jwtService = mockk()
        authenticationService = AuthenticationService(
            manager = manager,
            userRepository = userRepository,
            uploadService = uploadService,
            passwordEncoder = passwordEncoder,
            jwtService = jwtService
        )
    }

    @Test
    fun `register should create user with profile picture`() {
        // Arrange
        val request = SignUpRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            name = "testuser",
            picture = MockMultipartFile("test.jpg", "files".toByteArray())
        )
        val expectedUrl = "https://example.com/avatar.jpg"
        val encodedPassword = "encodedPassword123"

        every { passwordEncoder.encode(request.password) } returns encodedPassword
        every { runBlocking { uploadService.upload(request.username, request.picture) }} returns expectedUrl
        every { userRepository.save(any()) } returns mockk()

        // Act
        authenticationService.register(request)

        // Assert
        verify { passwordEncoder.encode(request.password) }
        verify { runBlocking {uploadService.upload(request.username, request.picture) }}
        verify { 
            userRepository.save(match { user ->
                user.username == request.username &&
                user.email == request.email &&
                user.name == request.name &&
                user.password == encodedPassword &&
                user.profilePicture == expectedUrl
            })
        }
    }

    @Test
    fun `authenticate should return JWT token for valid credentials`() {
        // Arrange
        val request = SignInRequest(
            email = "test@example.com",
            password = "password123"
        )
        val user = mockk<User>()
        val userDetails = UserDetailsImpl(user)
        val expectedToken = "jwt.token.here"

        every { manager.authenticate(any()) } returns mockk {
            every { principal } returns userDetails
        }
        every { jwtService.generateToken(user) } returns expectedToken

        // Act
        val result = authenticationService.authenticate(request)

        // Assert
        assertNotNull(result)
        assertEquals(expectedToken, result)
        verify { manager.authenticate(any()) }
        verify { jwtService.generateToken(user) }
    }

    @Test
    fun `authenticate should throw BadRequestError for invalid credentials`() {
        // Arrange
        val request = SignInRequest(
            email = "test@example.com",
            password = "wrongpassword"
        )

        every { manager.authenticate(any()) } throws mockk<AuthenticationException>()

        // Act & Assert
        assertThrows<BadRequestError> {
            authenticationService.authenticate(request)
        }
    }
} 