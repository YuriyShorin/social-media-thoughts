package ru.shorin.authenticationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication(scanBasePackages = ["ru.shorin"])
@EnableDiscoveryClient
class AuthenticationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthenticationServiceApplication>(*args)
}
