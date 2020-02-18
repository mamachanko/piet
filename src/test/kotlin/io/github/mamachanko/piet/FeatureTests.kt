package io.github.mamachanko.piet

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType


@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = [
            "spring.datasource.url=jdbc:tc:postgresql:12.1-alpine:///",
            "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
        ]
)
class FeatureTests {

    @LocalServerPort
    private var serverPort: Int = -1

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    internal fun `Piet can get the text out of an image`() {
        val creationResponse = restTemplate.postForEntity(
                "/api/images",
                mapOf("url" to "http://localhost:$serverPort/test-image.png"),
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
        assertThat(image.selfLink).isNotBlank()
        assertThat(image.status).isEqualToIgnoringCase("complete")
        assertThat(image.text).containsIgnoringCase("optical character recognition")
    }

    data class Image(
            val status: String,
            val text: String?,
            val _links: Map<String, Map<String, String>>
    ) {
        val selfLink: String
            get() = _links.getValue("self").getValue("href")
    }
}
