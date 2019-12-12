package io.github.mamachanko.piet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PietApplication

fun main(args: Array<String>) {
    runApplication<PietApplication>(*args)
}
