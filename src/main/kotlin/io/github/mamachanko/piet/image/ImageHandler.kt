package io.github.mamachanko.piet.image

import org.springframework.context.event.EventListener
import org.springframework.data.rest.core.event.AfterCreateEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ImageHandler(
        private val imagePipeline: ImagePipeline,
        private val imageRepository: ImageRepository
) {

    @Async
    @EventListener
    fun onImage(event: AfterCreateEvent) {
        val processedImage = imagePipeline.process((event.source as Image))
        imageRepository.save(processedImage.copy(status = ImageStatus.Complete))
    }
}
