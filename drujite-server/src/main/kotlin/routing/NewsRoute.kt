package routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.NewsModel
import requests.AddNewsRequest
import responses.IdResponse
import responses.NewsResponse
import services.NewsService

fun Route.newsRoute(newsService: NewsService) {
    authenticate {
        post {
            val request = call.receive<AddNewsRequest>()
            val news = request.toModel()
            val newsId = newsService.add(news)
            call.application.environment.log
                .info("News $newsId added")
            call.respond(HttpStatusCode.OK, IdResponse(newsId))
        }

        delete {
            val id =
                call.request.queryParameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                )
            val result = newsService.delete(id)
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
            val news = newsService.get(id)
            if (news != null) {
                call.respond(HttpStatusCode.OK, news.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/session") {
            val sessionId =
                call.request.queryParameters["sessionId"]?.toIntOrNull() ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                )
            val newsList = newsService.getSessionsNews(sessionId)
            call.respond(HttpStatusCode.OK, newsList.map { it.toResponse() })
        }
    }
}

private fun NewsModel.toResponse() =
    NewsResponse(
        id = id,
        sessionId = sessionId,
        title = title,
        content = content,
        dateTime = dateTime,
        imageUrl = imageUrl,
    )

private fun AddNewsRequest.toModel() =
    NewsModel(
        id = 0,
        sessionId = sessionId,
        title = title,
        content = content,
        dateTime = "",
        imageUrl = imageUrl,
    )
