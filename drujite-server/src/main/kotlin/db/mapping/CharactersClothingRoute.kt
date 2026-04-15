package ru.drujite.db.mapping

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
        post {
            val request = call.receive<AddCharactersClothingRequest>()
            val result =
                clothingService.addClothingItemsToCharacter(
                    characterId = request.characterId,
                    itemsIds = request.itemsIds,
                )
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Character or clothing item not found")
            }
        }

        get {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val clothingItems = clothingService.getCharactersClothing(id)
                call.respond(HttpStatusCode.OK, clothingItems)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid character ID")
            }
        }
    }
}
