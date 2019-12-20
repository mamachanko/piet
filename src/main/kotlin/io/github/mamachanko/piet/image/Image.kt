package io.github.mamachanko.piet.image

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

enum class ImageStatus {
    New,
    Complete
}

@Entity
data class Image(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        val id: String,
        val status: ImageStatus,
        @Column(columnDefinition = "TEXT")
        val text: String?,
        val url: String?
) {
    constructor() : this(
            id = "",
            status = ImageStatus.New,
            text = null,
            url = null
    )
}

data class ImageProcessingRequested(val image: Image, val content: ByteArray)

data class ImageProcessingCompleted(val image: Image)
