package ru.project.f1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class F1Application

fun main(args: Array<String>) {
	runApplication<F1Application>(*args)
}
