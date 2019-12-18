package io.github.mamachanko.piet.ocr

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.github.mamachanko.piet.Image
import io.github.mamachanko.piet.ImageCreated
import io.github.mamachanko.piet.ImageProcessed
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.util.ResourceUtils

class OcrServiceTest {

    private lateinit var ocrService: OcrService
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @BeforeEach
    internal fun setUp() {
        applicationEventPublisher = mock()
        ocrService = OcrService(applicationEventPublisher)
    }

    @Test
    internal fun `handleImageCreated - should recognize image text and publish event`() {
        ocrService.handleImageCreated(
                ImageCreated(
                        Image(content = ResourceUtils.getFile("classpath:test-image.png").readBytes())
                )
        )

        argumentCaptor<ImageProcessed>().apply {
            verify(applicationEventPublisher).publishEvent(capture())

            val testImageRegex = Regex(
                    ".*optical\\scharacter\\srecognition.*",
                    setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
            )
            assertThat(firstValue.image.text!!.matches(testImageRegex)).isTrue()
            assertThat(allValues.size).isEqualTo(1)
        }
    }
}
