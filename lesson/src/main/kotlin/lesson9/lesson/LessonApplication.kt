package lesson9.lesson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LessonApplication

fun main(args: Array<String>) {
	runApplication<LessonApplication>(*args)
}
