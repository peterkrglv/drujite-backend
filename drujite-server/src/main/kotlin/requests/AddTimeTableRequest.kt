package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddTimeTableRequest(
    val date: String,
    val sessionId: Int,
)
