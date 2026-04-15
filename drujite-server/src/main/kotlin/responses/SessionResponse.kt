package responses

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val description: String,
    val endDate: String,
    val id: Int,
    val name: String,
    val startDate: String,
    val imageUrl: String? = null,
    val qrLink: String? = null,
)
