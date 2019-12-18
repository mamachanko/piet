package io.github.mamachanko.piet

import com.fasterxml.jackson.annotation.JsonIgnore
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
        val id: String = "",
        val status: ImageStatus = ImageStatus.New,
        @Column(columnDefinition="TEXT")
        val text: String? = null,
        @JsonIgnore
        val content: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (status != other.status) return false
        if (text != other.text) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + content.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "Image(id='$id', status=$status, text=$text, content=[${content.take(5).joinToString(", ")}, ...])"
    }

}

data class ImageCreated(val image: Image)

data class ImageProcessed(val image: Image)
