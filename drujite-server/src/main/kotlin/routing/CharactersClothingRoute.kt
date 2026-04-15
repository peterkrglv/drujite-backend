package ru.drujite.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import ru.drujite.requests.AddCharactersClothingRequest
import ru.drujite.services.ClothingService

fun Route.charactersClothingRoute(clothingService: ClothingService) {
    authenticate {
        /**
         * Tag: CharactersClothing
         */
        get {
            val characterId =
                call.request.queryParameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
            val clothingItems = clothingService.getCharactersClothing(characterId)
            call.respond(HttpStatusCode.OK, clothingItems)
        }

        /**
         * Tag: CharactersClothing
         */
        get("editable") {
            val characterId =
                call.request.queryParameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
            val clothingItems = clothingService.getCharactersEditableClothingItems(characterId)
            call.respond(HttpStatusCode.OK, clothingItems)
        }

        /**
         * Tag: CharactersClothing
         */
        post {
            val request = call.receive<AddCharactersClothingRequest>()
            val characterId = request.characterId
            val itemsIds = request.itemsIds
            val result = clothingService.addClothingItemsToCharacter(characterId, itemsIds)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
