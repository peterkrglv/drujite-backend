package services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import models.UserModel
import requests.LoginRequest
import ru.drujite.util.SecurityUtils
import java.util.Date

class JwtService(
    private val application: Application,
    private val userService: UserService,
) {
    private companion object {
        private const val JWT_VALIDITY_HOURS = 72L
        private const val MILLIS_PER_SECOND = 1000L
        private const val SECONDS_PER_MINUTE = 60L
        private const val MINUTES_PER_HOUR = 60L
        private const val JWT_VALIDITY_MS =
            JWT_VALIDITY_HOURS * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIS_PER_SECOND
    }

    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")
    val jwtVerifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    suspend fun createJwtToken(loginRequest: LoginRequest): String? {
        val foundUser: UserModel? = userService.findByPhone(loginRequest.phone)
        println("loginrequest: $loginRequest, foundUser: $foundUser")
        return if (foundUser != null && SecurityUtils.verifyPassword(loginRequest.password, foundUser.password)) {
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("id", foundUser.id.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + JWT_VALIDITY_MS))
                .sign(Algorithm.HMAC256(secret))
        } else {
            null
        }
    }

    suspend fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val id: String? = extractId(credential)
        val foundUser: UserModel? = id?.let { userService.findById(it) }
        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }
    }

    private fun audienceMatches(credential: JWTCredential): Boolean = credential.payload.audience.contains(audience)

    private fun getConfigProperty(path: String) =
        application.environment.config
            .property(path)
            .getString()

    fun extractId(credential: JWTCredential): String? = credential.payload.getClaim("id").asString()

    fun extractId(principal: JWTPrincipal): String? = principal.payload.getClaim("id").asString()
}
