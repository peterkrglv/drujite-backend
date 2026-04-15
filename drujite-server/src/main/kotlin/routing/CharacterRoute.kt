package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import models.CharacterModel
import requests.AddCharacterRequest
import responses.CharacterResponse
import responses.IdResponse
import ru.drujite.requests.ChangeCharactersStoryRequest
import services.CharacterService

fun Route.characterRoute(characterService: CharacterService) {
    authenticate {
        /**
         * Tag: Character
         */
        get {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val character = characterService.getCharacter(id)
            if (character == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, character.toResponse())
            }
        }

        /**
         * Tag: Character
         */
        post {
            val request = call.receive<AddCharacterRequest>()
            val character =
                CharacterModel(
                    id = 0,
                    name = request.name,
                    story = request.story,
                    clanId = request.clanId,
                    imageUrl = request.image,
                )
            val characterId = characterService.addCharacter(character)
            call.respond(HttpStatusCode.Created, IdResponse(characterId))
        }

        /**
         * Tag: Character
         */
        delete {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                )
            val result = characterService.deleteCharacter(id)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Tag: Character
         */
        get("/with-clan-and-player") {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val character = characterService.getCharacterWithClanAndUser(id)
            if (character == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, character)
            }
        }

        /**
         * Tag: Character
         */
        put("story") {
            val request = call.receive<ChangeCharactersStoryRequest>()
            val result = characterService.changeCharactersStory(request.characterId, request.story)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun CharacterModel.toResponse() =
    CharacterResponse(
        id = this.id,
        name = this.name,
        story = this.story,
        clanId = this.clanId,
        imageUrl = this.imageUrl,
    )
