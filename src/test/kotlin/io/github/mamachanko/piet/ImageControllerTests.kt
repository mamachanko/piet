package io.github.mamachanko.piet

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ImageControllerTests {

    private lateinit var mockMvc: MockMvc
    private lateinit var imageService: ImageService

    @BeforeEach
    fun setUp() {
        imageService = mock()
        mockMvc = MockMvcBuilders
                .standaloneSetup(ImageController(imageService))
                .build()
    }

    @Test
    fun `POST - should create image`() {
        given(imageService.create(any())).willReturn(Image("test-image-id", ImageStatus.New, null, "image-content".toByteArray()))

        mockMvc.post("/api/images") {
            content = "image-content".toByteArray()
            contentType = MediaType.IMAGE_PNG
        }.andExpect {
            status { isCreated }
            header { string("Location", "/api/images/test-image-id") }
        }
        verify(imageService).create("image-content".toByteArray())
    }

    @Test
    fun `GET - should return image`() {
        given(imageService.get(any())).willReturn(Image("test-image-id", ImageStatus.New, "test-recognized-text", "image-content".toByteArray()))

        mockMvc.get("/api/images/test-image-id")
                .andExpect {
                    status { isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") { value("test-image-id") }
                    jsonPath("$.text") { value("test-recognized-text") }
                    jsonPath("$.status") { value("New") }
                }
        verify(imageService).get("test-image-id")
    }

    @Test
    fun `GET - should return 404 when image id is unknown`() {
        given(imageService.get(any())).willReturn(null)

        mockMvc.get("/api/images/test-image-id")
                .andDo { print() }
                .andExpect {
                    status { isNotFound }
                }
        verify(imageService).get("test-image-id")
    }
}