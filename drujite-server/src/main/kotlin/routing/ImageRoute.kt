package ru.drujite.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import ru.drujite.services.ImageService

fun Route.imageRoute(imageService: ImageService) {
    authenticate {
        /**
         * Tag: Images
         */
        post("/{entityType}/{id}") {
            val entityType =
                call.parameters["entityType"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Entity type is required",
                )
            val id =
                call.parameters["id"]?.toIntOrNull() ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid ID",
                )

            val fileBytes = call.receive<ByteArray>()
            val success = imageService.saveImage(entityType, id, fileBytes, "jpg")
            if (success) {
                call.respond(HttpStatusCode.OK, "Image uploaded successfully")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to save image")
            }
        }
    }

    /**
     * Tag: Images
     */
    get("/{entityType}/{id}") {
        val entityType =
            call.parameters["entityType"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Entity type is required",
            )
        val id =
            call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Invalid ID",
            )

        val file = imageService.getImagePath(entityType, id)
        if (file != null && file.exists()) {
            call.respondFile(file)
        } else {
            call.respond(HttpStatusCode.NotFound, "File not found")
        }
    }
}
