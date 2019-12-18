package io.github.mamachanko.piet

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ImageRepositoryTests {

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Test
    internal fun `should save image`() {
        val image = Image(
                status = ImageStatus.New,
                text = null,
                content = "image-content".toByteArray()
        )

        val savedImage = imageRepository.save(image)
        val retrievedImage = imageRepository.findById(savedImage.id).get()

        assertThat(retrievedImage.id).isNotBlank()
        assertThat(retrievedImage.status).isEqualTo(ImageStatus.New)
        assertThat(retrievedImage.content).isEqualTo("image-content".toByteArray())
        assertThat(retrievedImage.text).isNull()
    }
}
