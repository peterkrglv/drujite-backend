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
import requests.AddUserSessionCharacter
import services.CharacterService
import services.JwtService
import services.UsersSessionsService

fun Route.usersCharactersRoute(
    jwtService: JwtService,
    usersSessionsService: UsersSessionsService,
    characterService: CharacterService,
) {
    authenticate {
        /**
         * Tag: UsersCharacters
         */
        get {
            val sessionId =
                call.request.queryParameters["sessionId"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val character =
                usersSessionsService.getCharacter(
                    userId = userId,
                    sessionId = sessionId,
                ) ?: return@get call.respond(HttpStatusCode.NotFound)
            val response =
                characterService.getCharacterWithClanAndUser(character.id) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                )
            call.respond(HttpStatusCode.OK, response)
        }

        /**
         * Tag: UsersCharacters
         */
        post {
            val characterRequest = call.receive<AddUserSessionCharacter>()
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@post call.respond(HttpStatusCode.Unauthorized)
            usersSessionsService.addCharacter(
                userId = userId,
                sessionId = characterRequest.sessionId,
                characterId = characterRequest.characterId,
            )
            call.respond(HttpStatusCode.Created)
        }

        /**
         * Tag: UsersCharacters
         */
        get("/user-all") {
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val characters = usersSessionsService.getCharacters(userId)
            val response = characters.map { characterService.getCharacterWithClanAndUser(it.id) }
            call.respond(HttpStatusCode.OK, response)
        }

        /**
         * Tag: UsersCharacters
         */
        get("session-all") {
            val id =
                call.request.queryParameters["sessionId"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing 'id' parameter")
            val characters = usersSessionsService.getSessionsCharacters(id)
            val response = characters.map { characterService.getCharacterWithClanAndUser(it.id) }
            call.respond(HttpStatusCode.OK, response)
        }

        /**
         * Tag: UsersCharacters
         */
        delete {
            val characterRequest = call.receive<AddUserSessionCharacter>()
            val principal = call.principal<JWTPrincipal>()
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@delete call.respond(HttpStatusCode.Unauthorized)
            val result =
                usersSessionsService.deleteCharacter(
                    userId = userId,
                    sessionId = characterRequest.sessionId,
                    characterId = characterRequest.characterId,
                )
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
