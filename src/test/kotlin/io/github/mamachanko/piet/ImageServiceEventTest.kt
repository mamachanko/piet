package io.github.mamachanko.piet

import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = [ImageService::class]
)
class ImageServiceEventTest {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @MockBean
    private lateinit var imageService: ImageService

    @Test
    internal fun `should handle ImageProcessed events`() {
        applicationEventPublisher.publishEvent(ImageProcessed(Image(id = "test-image-id", content = "test-image-content".toByteArray())))

        verify(imageService).handleImageProcessed(ImageProcessed(Image(id = "test-image-id", content = "test-image-content".toByteArray())))
    }
}