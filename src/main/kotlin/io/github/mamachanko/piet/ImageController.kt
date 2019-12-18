package io.github.mamachanko.piet

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class ImageController(private val imageService: ImageService) {

    @PostMapping("/api/images")
    fun createImage(@RequestBody content: ByteArray): ResponseEntity<String> =
            imageService.create(content).let { image ->
                ResponseEntity
                        .created(UriComponentsBuilder
                                .fromUriString("/api/images/${image.id}")
                                .build()
                                .toUri())
                        .build<String>()
            }

    @GetMapping("/api/images/{id}")
    fun getImage(@PathVariable id: String): ResponseEntity<Image> =
            when (val image = imageService.get(id)) {
                null -> ResponseEntity.notFound().build()
                else -> ResponseEntity.ok(image)
            }

}
