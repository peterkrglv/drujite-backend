package requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTimetableBySessionAndDate(
    val date: String,
    val sessionId: Int,
)
