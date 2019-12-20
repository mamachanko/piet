package io.github.mamachanko.piet.content

import io.github.mamachanko.piet.image.Image
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    private lateinit var contentService: ContentService

    @BeforeEach
    internal fun setUp() {
        restTemplate = RestTemplate()
        mockServer = MockRestServiceServer.bindTo(restTemplate).build()

        contentService = ContentService(restTemplate)
    }

    @Test
    internal fun `handleImageCreated - should fetch image content and publish event`() {
        mockServer
                .expect(ExpectedCount.once(), requestTo("http://image.source/test-image-url"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("test-image-content".toByteArray(), MediaType.IMAGE_PNG))

        val content = contentService.fetch(Image().copy(url = "http://image.source/test-image-url"))

        assertThat(content).isEqualTo("test-image-content".toByteArray())
        mockServer.verify()
    }

}