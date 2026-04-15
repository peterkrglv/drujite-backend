package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.SessionModel
import requests.IdRequest
import responses.SessionResponse
import ru.drujite.requests.AddSessionByQRRequest
import services.JwtService
import services.UsersSessionsService

fun Route.usersSessionsRoute(
    jwtService: JwtService,
    usersSessionsService: UsersSessionsService,
) {
    authenticate {
        /**
         * Tag: UsersSessions
         */
        get {
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val sessions = usersSessionsService.getUsersSessions(userId)
            call.respond(HttpStatusCode.OK, sessions.map { it.toResponse() })
        }

        /**
         * Tag: UsersSessions
         */
        post {
            val sessionRequest = call.receive<IdRequest>()
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@post call.respond(HttpStatusCode.Unauthorized)
            usersSessionsService.addUserSession(userId = userId, sessionId = sessionRequest.id)
            call.respond(HttpStatusCode.Created)
        }

        /**
         * Tag: UsersSessions
         */
        post("qr") {
            val request = call.receive<AddSessionByQRRequest>()
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val session = usersSessionsService.addUsersSessionByQr(userId, request.qr)
            if (session != null) {
                call.respond(HttpStatusCode.Created, session.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun SessionModel.toResponse(): SessionResponse =
    SessionResponse(
        description = this.description,
        endDate = this.endDate,
        id = this.id,
        name = this.name,
        startDate = this.startDate,
        imageUrl = this.imageUrl,
        qrLink = this.qrLink,
    )
