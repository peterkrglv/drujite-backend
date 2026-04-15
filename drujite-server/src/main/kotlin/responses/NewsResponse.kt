package responses

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val content: String,
    val dateTime: String,
    val id: Int,
    val imageUrl: String?,
    val sessionId: Int,
    val title: String,
)
