package io.github.mamachanko.piet.ocr

import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import java.nio.file.Files

class TempFile(private val content: ByteArray) : Closeable {

    private val logger = LoggerFactory.getLogger(javaClass)

    val file: File =
            Files.createTempFile("ocr", ".png").run {
                logger.info("created $this")
                logger.info("writing content to $this")
                Files.write(toAbsolutePath(), content)
            }.toFile()

    override fun close() {
        logger.info("deleting $this")
        Files.delete(file.toPath())
    }

    override fun toString(): String {
        return "TempFile(file=$file)"
    }
}