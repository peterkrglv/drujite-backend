package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddUserSessionCharacter(
    val sessionId: Int,
    val characterId: Int,
)
