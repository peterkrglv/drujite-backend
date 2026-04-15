package requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTimetableBySessionAndDate(
    val sessionId: Int,
    val date: String,
)
