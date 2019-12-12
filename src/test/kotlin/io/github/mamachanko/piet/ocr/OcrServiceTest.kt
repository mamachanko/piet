package io.github.mamachanko.piet.ocr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.util.ResourceUtils

class OcrServiceTest {

    @Test
    internal fun `recognize - should return recognized characters image byte array`() {
        val testImage = ResourceUtils.getFile("classpath:ocr-test.png").readBytes()

        val recognizedText = OcrService().recognize(testImage).trim()

        assertThat(recognizedText).matches(".*OCR\\sis\\sshort\\sfor\\s\"optical\\scharacter\\srecognition\".*")
    }
}
