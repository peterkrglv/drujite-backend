package routing

import io.ktor.http.ContentType
import io.ktor.openapi.OpenApiInfo
import io.ktor.server.application.Application
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.openapi.OpenApiDocSource
import io.ktor.server.routing.openapi.hide
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.routing.routingRoot
import ru.drujite.routing.adminAuthRoute
import ru.drujite.routing.authRoute
import ru.drujite.routing.charactersClothingRoute
import ru.drujite.routing.clothingRoute
import ru.drujite.routing.clothingTypeRoute
import ru.drujite.routing.imageRoute
import ru.drujite.routing.superAdminRoute
import ru.drujite.services.ClothingService
import ru.drujite.services.ImageService
import services.CharacterService
import services.ClanService
import services.GoalService
import services.JwtService
import services.NewsService
import services.SessionService
import services.TimeTableService
import services.UserService
import services.UsersSessionsService

private val apiDescription =
    """
    Автогенерация OpenAPI: пути и методы из роутинга, схемы — из kotlinx-serialization и вызовов `receive` / `respond`.
    Укажите JWT в Authorize (Bearer), если эндпоинт защищён.
    """.trimIndent()

private fun Route.mountOpenApiAndSwagger(
    docSource: OpenApiDocSource,
    v1Base: String,
) {
    val info =
        OpenApiInfo(
            description = apiDescription,
            title = "Drujite API",
            version = "1.0.0",
        )
    route(v1Base) {
        openAPI(path = "openapi") {
            this.info = info
            source = docSource
        }.hide()
        swaggerUI(path = "swagger") {
            this.info = info
            source = docSource
            version = "5.18.2"
            deepLinking = true
        }.hide()
    }
}

private fun Route.swaggerPathRedirects(v1Base: String) {
    val redirects =
        listOf(
            "$v1Base/openapi/" to "$v1Base/openapi",
            "$v1Base/swagger/" to "$v1Base/swagger",
            "/openapi" to "$v1Base/openapi",
            "/openapi/" to "$v1Base/openapi",
            "/swagger" to "$v1Base/swagger",
            "/swagger/" to "$v1Base/swagger",
        )
    for ((from, to) in redirects) {
        get(from) {
            call.respondRedirect(to)
        }.hide()
    }
}

@Suppress("LongParameterList")
private fun Route.registerV1Routes(
    v1: String,
    characterService: CharacterService,
    clothingService: ClothingService,
    clanService: ClanService,
    goalService: GoalService,
    imageService: ImageService,
    jwtService: JwtService,
    newsService: NewsService,
    sessionService: SessionService,
    timeTableService: TimeTableService,
    userService: UserService,
    usersSessionsService: UsersSessionsService,
) {
    route(v1 + "admin") {
        adminAuthRoute(jwtService, userService)
    }
    route(v1 + "auth") {
        authRoute(jwtService)
    }
    route(v1 + "character") {
        characterRoute(characterService)
    }
    route(v1 + "characters-clothing") {
        charactersClothingRoute(clothingService)
    }
    route(v1 + "clan") {
        clanRoute(clanService = clanService)
    }
    route(v1 + "clothing-item") {
        clothingRoute(clothingService)
    }
    route(v1 + "clothing-type") {
        clothingTypeRoute(clothingService)
    }
    route(v1 + "event") {
        eventRoute(timeTableService)
    }
    route(v1 + "goal") {
        goalRoute(goalService, jwtService)
    }
    route(v1 + "images") {
        imageRoute(imageService)
    }
    route(v1 + "news") {
        newsRoute(newsService)
    }
    route(v1 + "session") {
        sessionRoute(jwtService, sessionService)
    }
    route(v1 + "signup") {
        signupRoute(jwtService, userService)
    }
    route(v1 + "super-admin") {
        superAdminRoute(jwtService, userService)
    }
    route(v1 + "timetable") {
        timeTableRoute(timeTableService)
    }
    route(v1 + "user") {
        userRoute(userService, jwtService)
    }
    route(v1 + "users-characters") {
        usersCharactersRoute(jwtService, usersSessionsService, characterService)
    }
    route(v1 + "users-sessions") {
        usersSessionsRoute(jwtService, usersSessionsService)
    }
}

private fun Route.welcomePage(
    v1: String,
    v1Base: String,
) {
    // ignore!
    get(v1) {
        call.respondText(
            """
            <html>
                <head>
                    <title>Drujite API</title>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link rel="stylesheet" type="text/css" href="/static/styles.css">
                    <style>
                      body { font-family: system-ui, sans-serif; max-width: 42rem; margin: 2rem auto; padding: 0 1rem; line-height: 1.5; }
                      a { color: #2563eb; }
                      ul { padding-left: 1.2rem; }
                    </style>
                </head>
                <body>
                    <h1>Drujite API</h1>
                    <p>За южное солнце!</p>
                    <p><strong>Документация</strong></p>
                    <ul>
                      <li><a href="$v1Base/swagger">Swagger UI</a> — интерактивно, Try it out</li>
                      <li><a href="$v1Base/openapi">OpenAPI (HTML)</a></li>
                    </ul>
                </body>
            </html>
            """.trimIndent(),
            ContentType.Text.Html,
        )
    }
}

fun Application.configureRouting(
    characterService: CharacterService,
    clothingService: ClothingService,
    clanService: ClanService,
    goalService: GoalService,
    imageService: ImageService,
    jwtService: JwtService,
    newsService: NewsService,
    sessionService: SessionService,
    timeTableService: TimeTableService,
    userService: UserService,
    usersSessionsService: UsersSessionsService,
) {
    val v1 = "/api/v1/"
    val v1Base = "/api/v1"
    val routingDocSource =
        OpenApiDocSource.Routing(ContentType.Application.Json) {
            routingRoot.descendants()
        }

    routing {
        mountOpenApiAndSwagger(routingDocSource, v1Base)
        swaggerPathRedirects(v1Base)
        registerV1Routes(
            v1,
            characterService,
            clothingService,
            clanService,
            goalService,
            imageService,
            jwtService,
            newsService,
            sessionService,
            timeTableService,
            userService,
            usersSessionsService,
        )
        welcomePage(v1, v1Base)
    }
}
