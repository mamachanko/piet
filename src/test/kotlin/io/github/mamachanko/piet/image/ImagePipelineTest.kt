package io.github.mamachanko.piet.image

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import io.github.mamachanko.piet.content.ContentService
import io.github.mamachanko.piet.ocr.OcrService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE
)
class ImagePipelineTest {

    @Autowired
    private lateinit var imagePipeline: ImagePipeline

    @MockBean
    private lateinit var contentService: ContentService

    @MockBean
    private lateinit var ocrService: OcrService

    @Test
    fun `should process image`() {
        given(contentService.fetch(any())).willReturn("test-image-content".toByteArray())
        given(ocrService.recognize(any())).willReturn("test-recognized-text")

        val processedImage = imagePipeline.process(Image().copy(url = "test-image-url"))

        assertThat(processedImage).isEqualTo(Image().copy(url = "test-image-url", text = "test-recognized-text"))
        inOrder(contentService, ocrService) {
            verify(contentService).fetch(Image().copy(url = "test-image-url"))
            verify(ocrService).recognize("test-image-content".toByteArray())
        }
    }
}