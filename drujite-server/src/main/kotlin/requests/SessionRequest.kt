package requests

import kotlinx.serialization.Serializable

@Serializable
data class SessionRequest(
    val description: String,
    val endDate: String,
    val name: String,
    val startDate: String,
    val imageUrl: String? = null,
)
