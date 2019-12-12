package io.github.mamachanko.piet.ocr

import org.bytedeco.leptonica.global.lept
import org.bytedeco.tesseract.TessBaseAPI
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File

@Service
class OcrService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val tesseract = TessBaseAPI().apply {
        Init(ResourceUtils.getFile("classpath:tessdata/").absolutePath, "eng")
    }

    fun recognize(content: ByteArray): String =
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

