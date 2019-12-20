package io.github.mamachanko.piet.content

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.github.mamachanko.piet.image.Image
import io.github.mamachanko.piet.image.ImageProcessingRequested
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.rest.core.event.AfterCreateEvent
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate

internal class ContentServiceTest {

    private lateinit var restTemplate: RestTemplate
    private lateinit var mockServer: MockRestServiceServer
    private lateinit var applicationEventPublisher: ApplicationEventPublisher
    private lateinit var contentService: ContentService

    @BeforeEach
    internal fun setUp() {
        restTemplate = RestTemplate()
        mockServer = MockRestServiceServer.bindTo(restTemplate).build()
        applicationEventPublisher = mock()

        contentService = ContentService(restTemplate, applicationEventPublisher)
    }

    @Test
    internal fun `handleImageCreated - should fetch image content and publish event`() {
        mockServer
                .expect(ExpectedCount.once(), requestTo("http://image.source/test-image-url"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("test-image-content".toByteArray(), MediaType.IMAGE_PNG))

        contentService.handleImageCreated(
                AfterCreateEvent(Image().copy(url = "http://image.source/test-image-url"))
        )

        mockServer.verify()
        argumentCaptor<ImageProcessingRequested>().apply {
            verify(applicationEventPublisher).publishEvent(capture())

            assertThat(firstValue.image).isEqualTo(Image().copy(url = "http://image.source/test-image-url"))
            assertThat(firstValue.content).isEqualTo("test-image-content".toByteArray())
            assertThat(allValues.size).isEqualTo(1)
        }
    }

}