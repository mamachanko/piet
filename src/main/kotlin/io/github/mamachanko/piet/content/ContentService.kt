package io.github.mamachanko.piet.content

import io.github.mamachanko.piet.image.Image
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ContentService(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun fetch(image: Image): ByteArray {
        logger.info("Getting content for $image")
        return restTemplate.getForObject(image.url!!, ByteArray::class.java)!!
    }

}
