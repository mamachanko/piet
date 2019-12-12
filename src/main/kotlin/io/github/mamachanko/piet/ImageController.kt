package io.github.mamachanko.piet

import io.github.mamachanko.piet.ocr.OcrService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ImageController(private val ocrService: OcrService) {

    @PostMapping("/api/images")
    fun createImage(@RequestBody image: ByteArray): ResponseEntity<String> =
            ocrService.recognize(image).let { recognizedText ->
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(recognizedText)
            }
}
