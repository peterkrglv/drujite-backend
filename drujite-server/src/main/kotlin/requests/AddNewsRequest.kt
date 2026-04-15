package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddNewsRequest(
    val content: String,
    val sessionId: Int,
    val title: String,
    val imageUrl: String? = null,
)
