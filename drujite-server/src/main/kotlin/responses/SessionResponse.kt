package responses

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val imageUrl: String? = null,
    val qrLink: String? = null,
)
