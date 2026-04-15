package responses

import kotlinx.serialization.Serializable

@Serializable
class SessionsResponse(
    val sessions: List<SessionResponse>,
)
