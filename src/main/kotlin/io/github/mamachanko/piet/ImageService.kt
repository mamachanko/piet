package io.github.mamachanko.piet

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class ImageService(private val imageRepository: ImageRepository, val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(content: ByteArray): Image {
        val image = imageRepository.save(Image(content = content))
        applicationEventPublisher.publishEvent(ImageCreated(image))
        return image
    }

    fun get(id: String): Image? =
            imageRepository.findById(id).map { it }.orElse(null)

    @Async
    @EventListener
    fun handleImageProcessed(imageProcessed: ImageProcessed) {
        logger.info("Handling $imageProcessed")
        imageRepository.save(imageProcessed.image.copy(status = ImageStatus.Complete))
    }
}
