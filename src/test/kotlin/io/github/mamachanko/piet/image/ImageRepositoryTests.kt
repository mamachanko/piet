package io.github.mamachanko.piet.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest(properties = [
    "spring.datasource.url=jdbc:tc:postgresql:12.1-alpine:///",
    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
])
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ImageRepositoryTests {

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Test
    internal fun `should save image`() {
        val image = Image().copy(
                status = ImageStatus.New,
                text = "image-text",
                url = "image-url"
        )

        val savedImage = imageRepository.save(image)
        val retrievedImage = imageRepository.findById(savedImage.id).get()

        assertThat(retrievedImage.id).isNotBlank()
        assertThat(retrievedImage.status).isEqualTo(ImageStatus.New)
        assertThat(retrievedImage.url).isEqualTo("image-url")
        assertThat(retrievedImage.text).isEqualTo("image-text")
    }
}
