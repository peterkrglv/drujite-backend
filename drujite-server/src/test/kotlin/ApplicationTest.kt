package ru.drujite

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.yaml.YamlConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() =
        testApplication {
            environment {
                config = YamlConfig("application.yaml")!!
            }
            application {
                module()
            }
            client.get("/api/v1/").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }
}
