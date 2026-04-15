package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddTimeTableRequest(
    val sessionId: Int,
    val date: String,
)
