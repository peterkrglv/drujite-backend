package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.ClanModel
import requests.AddClanRequest
import requests.ClanToSessionRequest
import responses.ClanResponse
import responses.IdResponse
import services.ClanService

fun Route.clanRoute(clanService: ClanService) {
    authenticate {
        /**
         * Tag: Clan
         */
        get {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val clan = clanService.getClan(id)
            if (clan == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, clan.toResponse())
            }
        }

        /**
         * Tag: Clan
         */
        post {
            val request = call.receive<AddClanRequest>()
            val clan = request.toModel()
            val clanId = clanService.addClan(clan)
            call.respond(HttpStatusCode.Created, IdResponse(clanId))
        }

        /**
         * Tag: Clan
         */
        delete {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                )
            val result = clanService.deleteClan(id)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Tag: Clan
         */
        post("/add-to-session") {
            val request = call.receive<ClanToSessionRequest>()
            val result = clanService.addClanToSession(request.clanId, request.sessionId)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Tag: Clan
         */
        delete("/delete-from-session") {
            val request = call.receive<ClanToSessionRequest>()
            val result = clanService.deleteClanFromSession(request.clanId, request.sessionId)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Tag: Clan
         */
        get("/session-all") {
            val id =
                call.request.queryParameters["sessionId"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val clans = clanService.getSessionsClans(id)
            call.respond(HttpStatusCode.OK, clans.map { it.toResponse() })
        }

        /**
         * Tag: Clan
         */
        get("all") {
            val clans = clanService.getAllClans()
            call.respond(HttpStatusCode.OK, clans.map { it.toResponse() })
        }
    }
}

private fun AddClanRequest.toModel() =
    ClanModel(
        id = 0,
        name = this.name,
        description = this.description,
    )

private fun ClanModel.toResponse() =
    ClanResponse(
        id = this.id,
        name = this.name,
        description = this.description,
    )
