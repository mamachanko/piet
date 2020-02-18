package io.github.mamachanko.piet

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
        properties = [
            "spring.datasource.url=jdbc:tc:postgresql:12.1-alpine:///",
            "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
        ]
)
class PietApplicationTests {

    @Test
    fun contextLoads() {
    }

}
