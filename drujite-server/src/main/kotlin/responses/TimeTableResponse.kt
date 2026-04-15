package responses

import kotlinx.serialization.Serializable

@Serializable
data class TimeTableResponse(
    val id: Int,
    val sessionId: Int,
    val date: String,
)
