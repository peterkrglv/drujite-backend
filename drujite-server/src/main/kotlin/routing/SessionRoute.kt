package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.SessionModel
import requests.SessionRequest
import responses.IdResponse
import responses.SessionResponse
import services.JwtService
import services.SessionService

fun Route.sessionRoute(
    jwtService: JwtService,
    sessionService: SessionService,
) {
    authenticate {
        /**
         * Tag: Session
         */
        get("/user-sessions") {
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val sessions = sessionService.getSessionsByUserId(userId)
            call.respond(HttpStatusCode.OK, sessions.map { it.toResponse() })
        }

        /**
         * Tag: Session
         */
        get("/all") {
            val sessions = sessionService.getAllSessions()
            call.respond(HttpStatusCode.OK, sessions.map { it.toResponse() })
        }

        /**
         * Tag: Session
         */
        get {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val session =
                sessionService.getSession(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, session.toResponse())
        }

        /**
         * Tag: Session
         */
        post {
            val sessionRequest = call.receive<SessionRequest>()
            val id = sessionService.addSession(sessionRequest)
            call.respond(HttpStatusCode.Created, IdResponse(id = id))
        }

        /**
         * Tag: Session
         */
        delete {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                )
            sessionService.deleteSession(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}

private fun SessionModel.toResponse(): SessionResponse =
    SessionResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        startDate = this.startDate,
        endDate = this.endDate,
        imageUrl = this.imageUrl,
        qrLink = this.qrLink,
    )
