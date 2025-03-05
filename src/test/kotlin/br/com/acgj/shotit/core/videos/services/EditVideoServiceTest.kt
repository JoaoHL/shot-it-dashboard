package br.com.acgj.shotit.core.videos.services

import br.com.acgj.shotit.core.domain.*
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertTrue

class EditVideoServiceTest {
    private lateinit var videoRepository: VideoRepository
    private lateinit var tagsRepository: VideoCategoryTagRepository
    private lateinit var thumbnailRepository: ThumbnailRepository
    private lateinit var editVideoService: EditVideoService

    @BeforeEach
    fun setup(){
        videoRepository = mockk()
        tagsRepository = mockk()
        thumbnailRepository = mockk()
        editVideoService = EditVideoService(videoRepository, tagsRepository, thumbnailRepository)
    }

    @Test
    fun `given valid name updateVideoTitle should return true for update`(){
        val user = mockk<User>()
        val id = 1L
        val title = "Updated Title"

        every { videoRepository.updateVideoTitle(title, id, user) } returns 1

        val response = editVideoService.updateVideoTitle(user, id, title)

        assertTrue(response)
        verify { videoRepository.updateVideoTitle(title, id, user) }
    }

    @Test
    fun `updateVideoTitle should return false when update fails`() {
        val user = mockk<User>()
        val videoId = 1L
        val newTitle = "New Video Title"

        every { videoRepository.updateVideoTitle(newTitle, videoId, user) } returns 0

        val result = editVideoService.updateVideoTitle(user, videoId, newTitle)

        assertFalse(result)
        verify { videoRepository.updateVideoTitle(newTitle, videoId, user) }
    }

    @Test
    fun `updateVideoTags should update video tags`() {
        val videoId = 1L
        val tagIds = mutableListOf(1L, 2L)
        val video = mockk<Video>(relaxed = true)
        val tags = mutableListOf(mockk<VideoCategoryTag>(), mockk<VideoCategoryTag>())

        every { videoRepository.findById(videoId) } returns Optional.of(video)
        every { tagsRepository.findAllById(tagIds) } returns tags
        every { videoRepository.save(video) } returns video

        editVideoService.updateVideoTags(videoId, tagIds)

        verify { video.tags = tags }
        verify { videoRepository.save(video) }
    }

    @Test
    fun `updateVideoTags should throw NotFoundError if video is not found`() {
        val videoId = 1L
        val tagIds = mutableListOf(1L, 2L)

        every { videoRepository.findById(videoId) } returns Optional.empty()

        assertThrows<NotFoundError> { editVideoService.updateVideoTags(videoId, tagIds) }
    }

    @Test
    fun `changeThumbnail should update the principal thumbnail`() {
        val thumbnailId = 1L
        val video = mockk<Video>()
        val newThumbnail = mockk<Thumbnail>(relaxed = true)
        val currentThumbnail = mockk<Thumbnail>(relaxed = true)

        every { thumbnailRepository.findById(thumbnailId) } returns Optional.of(newThumbnail)
        every { newThumbnail.video } returns video
        every { thumbnailRepository.findCurrentFavorite(video) } returns Optional.of(currentThumbnail)
        every { thumbnailRepository.save(any()) } returns newThumbnail

        editVideoService.changeThumbnail(thumbnailId)

        verify { currentThumbnail.principal = false }
        verify { thumbnailRepository.save(currentThumbnail) }
        verify { newThumbnail.principal = true }
        verify { thumbnailRepository.save(newThumbnail) }
    }

    @Test
    fun `changeThumbnail should throw NotFoundError if thumbnail is not found`() {
        val thumbnailId = 1L

        every { thumbnailRepository.findById(thumbnailId) } returns Optional.empty()

        assertThrows<NotFoundError> { editVideoService.changeThumbnail(thumbnailId) }
    }



}