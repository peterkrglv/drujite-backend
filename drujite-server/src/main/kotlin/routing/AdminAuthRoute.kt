package ru.drujite.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import requests.LoginRequest
import routing.toModel
import ru.drujite.requests.SignupRequest
import services.JwtService
import services.UserService

fun Route.adminAuthRoute(
    jwtService: JwtService,
    userService: UserService,
) {
    /**
     * Tag: Admin
     */
    post("/login") {
        val user = call.receive<LoginRequest>()
        val foundUser =
            userService.findByPhone(user.phone)
                ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val isAdmin = userService.checkIsAdmin((foundUser.id).toString())
        if (!isAdmin) {
            return@post call.respond(HttpStatusCode.Unauthorized)
        }
        val token: String? = jwtService.createJwtToken(user)
        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized,
        )
    }

    /**
     * Tag: Admin
     */
    post("/signup") {
        val request = call.receive<SignupRequest>()
        val createdUser =
            userService.addUser(user = request.toModel())
                ?: return@post call.respond(HttpStatusCode.Conflict)
        userService.makeAdmin(uuid = createdUser.id)
            ?: return@post call.respond(HttpStatusCode.Conflict)
        val loginRequest =
            LoginRequest(
                password = request.password,
                phone = request.phone,
            )
        val token: String? = jwtService.createJwtToken(loginRequest)
        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Conflict,
        )
    }
}
