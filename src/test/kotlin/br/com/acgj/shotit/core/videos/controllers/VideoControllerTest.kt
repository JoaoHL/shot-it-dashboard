package br.com.acgj.shotit.core.videos.controllers

import br.com.acgj.shotit.InfraContainersForTestConfiguration
import br.com.acgj.shotit.ShotitApplicationErrorHandler
import br.com.acgj.shotit.core.domain.*
import br.com.acgj.shotit.core.infra.auth.ApplicationUser
import br.com.acgj.shotit.core.infra.auth.UserDetailsImpl
import br.com.acgj.shotit.core.videos.ports.VideoDTO
import br.com.acgj.shotit.core.videos.services.EditVideoService
import br.com.acgj.shotit.core.videos.services.RetrieveVideoService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.core.userdetails.UserDetailsService
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationUser::class, ShotitApplicationErrorHandler::class)
class VideoControllerTest : InfraContainersForTestConfiguration() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var retrieveVideoService: RetrieveVideoService

    @Autowired
    private lateinit var editVideoService: EditVideoService

    @Autowired
    private lateinit var applicationUser: ApplicationUser

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var videoRepository: VideoRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var testUser: User
    private lateinit var testVideo: Video

    @BeforeEach
    fun setup() {
        testUser = userRepository.saveAndFlush(User(
            username = "testuser",
            name = "Test User",
            email = "test@example.com",
            password = "password"
        ))

        testVideo = videoRepository.saveAndFlush(Video(
            name = "Test Video",
            user = testUser,
            status = VideoStatus.SUCCESS
        ))

        val userDetails = UserDetailsImpl(testUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        val securityContext = SecurityContextImpl()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)
    }

    @Test
    @Transactional
    fun `should return user videos when authenticated`() {
        mockMvc.perform(get("/api/videos"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(testVideo.id))
            .andExpect(jsonPath("$[0].name").value("Test Video"))
    }

    @Test
    @Transactional
    fun `should return video by id when exists`() {
        mockMvc.perform(get("/api/videos/${testVideo.id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testVideo.id))
            .andExpect(jsonPath("$.name").value("Test Video"))
    }

    @Test
    @Transactional
    fun `should return 404 when video not found`() {
        mockMvc.perform(get("/api/videos/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun `should download thumbnails as zip file`() {
        mockMvc.perform(get("/api/videos/${testVideo.id}/thumbnails/download"))
            .andExpect(status().isOk)
            .andExpect(header().string("Content-Type", "application/octet-stream"))
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"images.zip\""))
    }
} 