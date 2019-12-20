package io.github.mamachanko.piet.ocr

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.util.ResourceUtils

class OcrServiceTest {

    @Test
    internal fun `should recognize image text`() {
        val image = ResourceUtils.getFile("classpath:static/test-image.png").readBytes()

        val recognizedText = OcrService().recognize(image)

        val testImageRegex = Regex(
                ".*optical\\scharacter\\srecognition.*",
                setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )
        assertThat(recognizedText.matches(testImageRegex)).isTrue()
    }
}
