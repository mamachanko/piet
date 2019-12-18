package io.github.mamachanko.piet

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import java.util.Optional

class ImageServiceTest {

    private lateinit var imageService: ImageService
    private lateinit var imageRepository: ImageRepository
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @BeforeEach
    internal fun setUp() {
        imageRepository = mock()
        applicationEventPublisher = mock()
        imageService = ImageService(imageRepository, applicationEventPublisher)
    }

    @Test
    internal fun `create - should save image and publish event`() {
        given(imageRepository.save(any<Image>())).willReturn(Image("test-image-id", ImageStatus.New, null, "test-image-content".toByteArray()))

        val createdImage = imageService.create("test-image-content".toByteArray())

        assertThat(createdImage).isEqualTo(Image("test-image-id", ImageStatus.New, null, "test-image-content".toByteArray()))
        inOrder(imageRepository, applicationEventPublisher) {
            verify(imageRepository).save(Image(content = "test-image-content".toByteArray()))
            verify(applicationEventPublisher).publishEvent(ImageCreated(createdImage))
        }
    }

    @Test
    internal fun `get - should return image`() {
        given(imageRepository.findById(any())).willReturn(Optional.of(Image(id="test-image-id", content = "image-content".toByteArray())))

        val image = imageService.get("test-image-id")

        assertThat(image).isEqualTo(Image(id="test-image-id", content = "image-content".toByteArray()))
        verify(imageRepository).findById("test-image-id")
    }

    @Test
    internal fun `get - should return null if id is unknown`() {
        given(imageRepository.findById(any())).willReturn(Optional.empty())

        val image = imageService.get("test-image-id")

        assertThat(image).isNull()
        verify(imageRepository).findById("test-image-id")
    }

    @Test
    internal fun `handleImageProcessed - should update image as completed`() {
        imageService.handleImageProcessed(ImageProcessed(Image(id="test-image-id", text = "recognized-text", content = "image-content".toByteArray())))

        verify(imageRepository).save(Image(id="test-image-id", text = "recognized-text", content = "image-content".toByteArray(), status = ImageStatus.Complete))
    }
}
