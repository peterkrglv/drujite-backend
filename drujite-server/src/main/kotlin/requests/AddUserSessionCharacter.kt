package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddUserSessionCharacter(
    val characterId: Int,
    val sessionId: Int,
)
