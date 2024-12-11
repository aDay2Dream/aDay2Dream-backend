package com.aday2dream.aday2dream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.orm.hibernate5.LocalSessionFactoryBean

@SpringBootApplication
class Aday2dreamApplication

fun main(args: Array<String>) {
	runApplication<Aday2dreamApplication>(*args)
}

