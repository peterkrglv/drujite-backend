package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import requests.LoginRequest
import ru.drujite.requests.SignupRequest
import services.JwtService
import services.UserService

fun Route.signupRoute(
    jwtService: JwtService,
    userService: UserService,
) {
    post {
        val signupRequest = call.receive<SignupRequest>()

        userService.addUser(
            user = signupRequest.toModel(),
        ) ?: return@post call.respond(HttpStatusCode.Conflict)
        val loginRequest = LoginRequest(signupRequest.phone, signupRequest.password)
        val token: String? = jwtService.createJwtToken(loginRequest)
        token?.let {
            call.respond(hashMapOf("token" to token))
        } ?: call.respond(
            message = HttpStatusCode.Conflict,
        )
    }
}
