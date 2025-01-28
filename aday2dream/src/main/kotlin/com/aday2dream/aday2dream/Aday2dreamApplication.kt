package com.aday2dream.aday2dream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.aday2dream.aday2dream.*"])
@EnableJpaRepositories("com.aday2dream.aday2dream.*")
@ComponentScan(basePackages = ["com.aday2dream.aday2dream.*"])
@EntityScan("com.aday2dream.aday2dream.*")
class Aday2dreamApplication

fun main(args: Array<String>) {
	runApplication<Aday2dreamApplication>(*args)
}

