package io.github.mamachanko.piet.image

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class ImageService(private val imageRepository: ImageRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun handleImageProcessingCompleted(imageProcessed: ImageProcessingCompleted) {
        logger.info("Handling $imageProcessed")
        imageRepository.save(imageProcessed.image.copy(status = ImageStatus.Complete))
    }
}
