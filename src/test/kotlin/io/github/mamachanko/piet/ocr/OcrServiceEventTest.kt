package io.github.mamachanko.piet.ocr

import com.nhaarman.mockitokotlin2.verify
import io.github.mamachanko.piet.image.Image
import io.github.mamachanko.piet.image.ImageProcessingRequested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = [OcrService::class]
)
class OcrServiceEventTest {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @MockBean
    private lateinit var ocrService: OcrService

    @Test
    internal fun `should handle ImageCreated events`() {
        val event = ImageProcessingRequested(Image().copy(id = "test-image-id"), "test-image-content".toByteArray())

        applicationEventPublisher.publishEvent(event)

        verify(ocrService).handleImageProcessingRequested(event)
    }
}