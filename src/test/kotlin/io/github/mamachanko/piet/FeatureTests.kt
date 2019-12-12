package io.github.mamachanko.piet

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.ResourceUtils

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class FeatureTests {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    internal fun `Piet can get the text out of an image`() {
        val testImage = ResourceUtils.getFile("classpath:ocr-test.png")

        val response = restTemplate.postForEntity(
                "/api/images",
                HttpEntity(
                        testImage.readBytes(),
                        HttpHeaders().apply { contentType = MediaType.IMAGE_PNG }
                ),
                String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.headers.contentType).isEqualTo(MediaType.valueOf("text/plain;charset=UTF-8"))
        assertThat(response.body).contains("OCR")
    }
}