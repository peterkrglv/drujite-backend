package responses

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val id: Int,
    val sessionId: Int,
    val title: String,
    val content: String,
    val dateTime: String,
    val imageUrl: String?,
)
