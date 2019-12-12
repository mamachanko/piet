package io.github.mamachanko.piet

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import io.github.mamachanko.piet.ocr.OcrService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ImageControllerTests {

    private lateinit var mockMvc: MockMvc
    private lateinit var ocrService: OcrService

    @BeforeEach
    fun setUp() {
        ocrService = mock()
        mockMvc = MockMvcBuilders
                .standaloneSetup(ImageController(ocrService))
                .build()
    }

    @Test
    fun `POST - should recognize text`() {
        given(ocrService.recognize(any<ByteArray>())).willReturn("recognized-text")

        mockMvc.post("/api/images") {
            content = "image-content".toByteArray()
            contentType = MediaType.IMAGE_PNG
        }.andExpect {
            status { isCreated }
            content { string("recognized-text") }
            content { contentType(MediaType.valueOf("text/plain;charset=ISO-8859-1")) }
        }
    }
}