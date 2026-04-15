package requests

import kotlinx.serialization.Serializable

@Serializable
data class ClanToSessionRequest(
    val clanId: Int,
    val sessionId: Int,
)
