package io.github.mamachanko.piet.image

import io.github.mamachanko.piet.content.ContentService
import io.github.mamachanko.piet.ocr.OcrService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.dsl.IntegrationFlow

@Configuration
class ImagePipelineConfig {

    @Bean
    fun images(ocrService: OcrService, contentService: ContentService): IntegrationFlow {
        return IntegrationFlow { f ->
            f.log().transform<Image, Pair<Image, ByteArray>> { image ->
                image to contentService.fetch(image)
            }.log().transform<Pair<Image, ByteArray>, Image> { (image, content) ->
                image.copy(text = ocrService.recognize(content))
            }.logAndReply()
        }
    }

}

@MessagingGateway
interface ImagePipeline {

    @Gateway(requestChannel = "images.input")
    fun process(image: Image): Image

}
