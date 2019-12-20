package io.github.mamachanko.piet.content

import com.nhaarman.mockitokotlin2.verify
import io.github.mamachanko.piet.image.Image
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.rest.core.event.AfterCreateEvent

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = [ContentService::class]
)
class ContentServiceEventTest {

    @MockBean
    private lateinit var contentService: ContentService

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Test
    internal fun should() {
        val afterCreateImageEvent = AfterCreateEvent(Image().copy(id = "test-image-id"))

        applicationEventPublisher.publishEvent(afterCreateImageEvent)

        verify(contentService).handleImageCreated(afterCreateImageEvent)
    }
}
