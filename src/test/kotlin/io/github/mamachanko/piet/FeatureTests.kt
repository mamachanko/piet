package io.github.mamachanko.piet

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.ResourceUtils

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FeatureTests {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    internal fun `Piet can get the text out of an image`() {
        val testImage = ResourceUtils.getFile("classpath:test-image.png")

        val creationResponse = restTemplate.postForEntity(
                "/api/images",
                HttpEntity(
                        testImage.readBytes(),
                        HttpHeaders().apply { contentType = MediaType.IMAGE_PNG }
                ),
                String::class.java
        )

        assertThat(creationResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(creationResponse.headers.location).isNotNull()
        val imageURI = creationResponse.headers.location!!

        val timeout = 50000L
        val started = System.currentTimeMillis()
        do {
            if ((System.currentTimeMillis() - started) >= timeout) {
                fail("Timed out after $timeout ms")
            } else {
                Thread.sleep(1000L)
            }

            val getResponse = restTemplate.getForEntity(imageURI, Image::class.java)
            assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(getResponse.headers.contentType).isEqualTo(MediaType.valueOf("application/json"))
        } while (getResponse.body!!.status.toLowerCase() != "complete")

        val image = restTemplate.getForObject(imageURI, Image::class.java)
        assertThat(image.id).isNotBlank()
        assertThat(image.status).isEqualToIgnoringCase("complete")
        assertThat(image.text).containsIgnoringCase("ocr")
    }

    data class Image(
            val id: String, // TODO: UUID
            val status: String,
            val text: String?
    )
}
