package io.github.mamachanko.piet.ocr

import io.github.mamachanko.piet.image.ImageProcessingRequested
import io.github.mamachanko.piet.image.ImageProcessingCompleted
import org.bytedeco.leptonica.global.lept
import org.bytedeco.tesseract.TessBaseAPI
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File

@Service
class OcrService(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val tesseract = TessBaseAPI().apply {
        Init(ResourceUtils.getFile("classpath:tessdata/").absolutePath, "eng")
    }

    @Async
    @EventListener
    fun handleImageProcessingRequested(imageCreated: ImageProcessingRequested) {
        logger.info("Handling $imageCreated")
        val recognizedText = recognize(imageCreated.content)
        applicationEventPublisher.publishEvent(ImageProcessingCompleted(imageCreated.image.copy(text = recognizedText)))
    }

    private fun recognize(content: ByteArray): String =
            TempFile(content).use {
                recognize(it.file)
            }

    private fun recognize(file: File): String {
        logger.info("recognizing text in $file ...")
        return lept.pixRead(file.absolutePath).let { image ->
            tesseract.SetImage(image)
            try {
                tesseract.GetUTF8Text().string.apply {
                    logger.info("recognized text \"$this\" in $file")
                }
            } finally {
                tesseract.Clear()
                lept.pixDestroy(image)
            }
        }
    }

}

