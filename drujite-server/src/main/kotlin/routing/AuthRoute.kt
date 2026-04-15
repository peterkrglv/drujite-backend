package ru.drujite.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import requests.LoginRequest
import services.JwtService

fun Route.authRoute(jwtService: JwtService) {
    /**
     * Tag: Auth
     */
    post {
        val user = call.receive<LoginRequest>()
        val token: String? = jwtService.createJwtToken(user)

        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized,
        )
    }
}
