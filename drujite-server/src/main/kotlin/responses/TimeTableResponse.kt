package responses

import kotlinx.serialization.Serializable

@Serializable
data class TimeTableResponse(
    val date: String,
    val id: Int,
    val sessionId: Int,
)
