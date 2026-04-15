package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import models.UserModel
import ru.drujite.requests.SignupRequest
import ru.drujite.responces.UserResponse
import services.JwtService
import services.UserService
import java.util.UUID

fun Route.userRoute(
    userService: UserService,
    jwtService: JwtService,
) {
    authenticate {
        /**
         * Tag: User
         */
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val foundUser =
                userService.findById(userId)
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                message = foundUser.toResponse(),
            )
        }
    }
}

fun SignupRequest.toModel(): UserModel =
    UserModel(
        gender = this.gender,
        id = UUID.randomUUID(),
        password = this.password,
        phone = this.phone,
        username = this.username,
    )

private fun UserModel.toResponse(): UserResponse =
    UserResponse(
        phone = this.phone,
        username = this.username,
    )
