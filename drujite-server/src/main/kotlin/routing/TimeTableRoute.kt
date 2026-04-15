package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.TimeTableModel
import requests.AddTimeTableRequest
import responses.IdResponse
import responses.TimeTableResponse
import services.TimeTableService

fun Route.timeTableRoute(timeTableService: TimeTableService) {
    authenticate {
        post {
            val request = call.receive<AddTimeTableRequest>()
            val timeTableId = timeTableService.addTimeTable(request.toModel())
            call.respond(HttpStatusCode.Created, IdResponse(timeTableId))
        }

        delete {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                )
            val result = timeTableService.deleteTimeTable(id)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val timeTable = timeTableService.getTimeTable(id)
            if (timeTable != null) {
                call.respond(HttpStatusCode.OK, timeTable.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/session-all") {
            val sessionId =
                call.request.queryParameters["sessionId"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val timeTables = timeTableService.getSessionsTimetables(sessionId)
            call.respond(
                HttpStatusCode.OK,
                timeTables.map { it.toResponse() },
            )
        }
    }
}

private fun TimeTableModel.toResponse() =
    TimeTableResponse(
        id = this.id,
        sessionId = this.sessionId,
        date = this.date,
    )

private fun AddTimeTableRequest.toModel() =
    TimeTableModel(
        id = 0,
        sessionId = sessionId,
        date = date,
    )
