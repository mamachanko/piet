package io.github.mamachanko.piet.content

import io.github.mamachanko.piet.image.Image
import io.github.mamachanko.piet.image.ImageProcessingRequested
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.data.rest.core.event.AfterCreateEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ContentService(
        private val restTemplate: RestTemplate,
        private val applicationEventPublisher: ApplicationEventPublisher
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun handleImageCreated(imageCreatedEvent: AfterCreateEvent) {
        logger.info("Handling $imageCreatedEvent")
        val image = imageCreatedEvent.source as Image
        val imageContent = restTemplate.getForObject(image.url!!, ByteArray::class.java)!!
        applicationEventPublisher.publishEvent(ImageProcessingRequested(image, imageContent))
    }

}
