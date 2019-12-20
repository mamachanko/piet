package io.github.mamachanko.piet.image

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ImageServiceTest {

    private lateinit var imageService: ImageService
    private lateinit var imageRepository: ImageRepository

    @BeforeEach
    internal fun setUp() {
        imageRepository = mock()
        imageService = ImageService(imageRepository)
    }

    @Test
    internal fun `handleImageProcessingCompleted - should update image as completed`() {
        imageService.handleImageProcessingCompleted(
                ImageProcessingCompleted(Image().copy(id = "test-image-id", status = ImageStatus.New))
        )

        verify(imageRepository).save(Image().copy(id = "test-image-id", status = ImageStatus.Complete))
    }
}
