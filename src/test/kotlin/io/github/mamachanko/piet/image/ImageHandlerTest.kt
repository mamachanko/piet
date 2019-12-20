package io.github.mamachanko.piet.image

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.rest.core.event.AfterCreateEvent

class ImageHandlerTest {

    @Test
    fun `should process and update image`() {
        val imagePipeline = mock<ImagePipeline>()
        val imageRepository = mock<ImageRepository>()

        given(imagePipeline.process(any())).willReturn(Image().copy(id = "test-image-id", text = "test-recognized-text"))

        ImageHandler(imagePipeline, imageRepository).onImage(AfterCreateEvent(Image().copy(id = "test-image-id")))

        inOrder(imagePipeline, imageRepository) {
            verify(imagePipeline).process(Image().copy(id = "test-image-id"))
            verify(imageRepository).save(Image().copy(id = "test-image-id", text = "test-recognized-text", status = ImageStatus.Complete))
        }
    }
}

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = [ImageHandler::class]
)
class ImageHandlerEventTest {

    @MockBean
    private lateinit var imageHandler: ImageHandler

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Test
    internal fun `should handle image creation event`() {
        val imageCreatedEvent = AfterCreateEvent(Image().copy(id = "test-image-id"))
        applicationEventPublisher.publishEvent(imageCreatedEvent)

        verify(imageHandler).onImage(imageCreatedEvent)
    }
}
