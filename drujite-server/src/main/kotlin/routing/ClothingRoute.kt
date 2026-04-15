package ru.drujite.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import responses.IdResponse
import ru.drujite.models.ClothingItem
import ru.drujite.requests.ClothingItemRequest
import ru.drujite.services.ClothingService

fun Route.clothingRoute(clothingService: ClothingService) {
    authenticate {
        post {
            val request = call.receive<ClothingItemRequest>()
            val clothingId = clothingService.addClothingItem(request.toModel())
            call.respond(HttpStatusCode.Created, IdResponse(clothingId))
        }

        delete {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                )
            val result = clothingService.deleteClothingItem(id)
            if (result) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Clothing item not found")
            }
        }

        get("/all") {
            val clothingItems = clothingService.getAllClothingItems()
            call.respond(HttpStatusCode.OK, clothingItems)
        }
    }
}

private fun ClothingItemRequest.toModel() =
    ClothingItem(
        id = 0,
        name = name,
        typeId = typeId,
        imageUrl = imageUrl,
        iconUrl = iconUrl,
    )
