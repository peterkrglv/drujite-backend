package ru.drujite.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import models.UserModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.drujite.requests.MakeAdminRequest
import ru.drujite.responses.UserAdminResponse
import services.JwtService
import services.UserService

fun Route.superAdminRoute(
    jwtService: JwtService,
    userService: UserService,
) {
    val logger: Logger = LoggerFactory.getLogger(Route::class.java)
    authenticate {
        /**
         * Tag: SuperAdmin
         */
        post("make-admin") {
            logger.info("Received request to make an admin")
            println("Received request to make an admin")
            val principal = call.principal<JWTPrincipal>()
            logger.info("Principal extracted: $principal")
            println("Principal extracted: $principal")
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@post call.respond(HttpStatusCode.Unauthorized)
            logger.info("User ID is $userId")
            println("User ID is $userId")
            if (!userService.checkIfSuperAdmin(userId)) {
                return@post call.respond(HttpStatusCode.Forbidden)
            }
            logger.info("User $userId is a super admin, proceeding with making an admin")
            println("User ID is $userId")
            val request = call.receive<MakeAdminRequest>()
            logger.info("Received request to make admin for phone: ${request.phone}")
            println("Received request to make admin for phone: ${request.phone}")
            val user =
                userService.findByPhone(request.phone)
                    ?: return@post call.respond(HttpStatusCode.NotFound)
            logger.info("Found user with phone ${request.phone}, ID: ${user.id}")
            println("Found user with phone ${request.phone}, ID: ${user.id}")
            if (userService.makeAdmin(user.id)) {
                logger.info("Successfully made user with phone ${request.phone} an admin")
                println("Successfully made user with phone ${request.phone} an admin")
                call.respond(HttpStatusCode.OK)
            } else {
                logger.error("Failed to make user with phone ${request.phone} an admin")
                println("Failed to make user with phone ${request.phone}")
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        /**
         * Tag: SuperAdmin
         */
        get("get-users") {
            logger.info("Received request to fetch all users")
            println("Received request to fetch all users")
            val principal = call.principal<JWTPrincipal>()
            logger.info("Fetching users for principal: $principal")
            println("Fetching users for principal: $principal")
            val userId =
                principal?.let { jwtService.extractId(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)
            logger.info("User ID is $userId")
            println("User ID is $userId")
            if (!userService.checkIfSuperAdmin(userId)) {
                return@get call.respond(HttpStatusCode.Forbidden)
            }
            logger.info("User $userId is a super admin, proceeding to fetch all users")
            println("User $userId is a super admin, proceeding to fetch all users")
            val users = userService.getAllUsers()
            logger.info("Fetched ${users.size} users")
            println("Fetched ${users.size} users")
            call.respond(users.map { it.toResponse() })
        }
    }
}

private fun UserModel.toResponse(): UserAdminResponse =
    UserAdminResponse(
        isAdmin = isAdmin,
        phone = phone,
        username = username,
    )
