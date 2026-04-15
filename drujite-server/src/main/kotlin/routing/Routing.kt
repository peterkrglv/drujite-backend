package routing

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
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

fun Application.configureRouting(
    userService: UserService,
    jwtService: JwtService,
    sessionService: SessionService,
    usersSessionsService: UsersSessionsService,
    characterService: CharacterService,
    goalService: GoalService,
    timeTableService: TimeTableService,
    clanService: ClanService,
    newsService: NewsService,
    imageService: ImageService,
    clothingService: ClothingService,
) {
    val v1 = "/api/v1/"
    routing {
        route(v1 + "user") {
            userRoute(userService, jwtService)
        }

        route(v1 + "auth") {
            authRoute(jwtService)
        }

        route(v1 + "signup") {
            signupRoute(jwtService, userService)
        }

        route(v1 + "session") {
            sessionRoute(jwtService, sessionService)
        }

        route(v1 + "users-sessions") {
            usersSessionsRoute(jwtService, usersSessionsService)
        }

        route(v1 + "users-characters") {
            usersCharactersRoute(jwtService, usersSessionsService, characterService)
        }

        route(v1 + "character") {
            characterRoute(characterService)
        }

        route(v1 + "goal") {
            goalRoute(goalService, jwtService)
        }

        route(v1 + "timetable") {
            timeTableRoute(timeTableService)
        }

        route(v1 + "event") {
            eventRoute(timeTableService)
        }

        route(v1 + "clan") {
            clanRoute(clanService = clanService)
        }

        route(v1 + "news") {
            newsRoute(newsService)
        }

        route(v1 + "images") {
            imageRoute(imageService)
        }

        route(v1 + "admin") {
            adminAuthRoute(jwtService, userService)
        }

        route(v1 + "clothing-type") {
            clothingTypeRoute(clothingService)
        }

        route(v1 + "clothing-item") {
            clothingRoute(clothingService)
        }

        route(v1 + "characters-clothing") {
            charactersClothingRoute(clothingService)
        }

        route(v1 + "super-admin") {
            superAdminRoute(jwtService, userService)
        }

        get(v1) {
            call.respondText(
                """
                <html>
                    <head>
                        <title>Drujite API</title>
                        <link rel="stylesheet" type="text/css" href="/static/styles.css">
                    </head>
                    <body>
                        <h1>Welcome to Drujite API</h1>
                        <p>За южное солнце!</p>
                        <p>${call.application.environment.config.property("jwt.secret").getString()}</p>
                    </body>
                </html>
                """.trimIndent(),
                ContentType.Text.Html,
            )
        }
    }
}
