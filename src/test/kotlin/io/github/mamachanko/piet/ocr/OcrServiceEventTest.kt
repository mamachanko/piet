package io.github.mamachanko.piet.ocr

import com.nhaarman.mockitokotlin2.verify
import io.github.mamachanko.piet.Image
import io.github.mamachanko.piet.ImageCreated
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
        applicationEventPublisher.publishEvent(ImageCreated(Image(id = "test-image-id", content = "test-image-content".toByteArray())))

        verify(ocrService).handleImageCreated(ImageCreated(Image(id = "test-image-id", content = "test-image-content".toByteArray())))
    }
}