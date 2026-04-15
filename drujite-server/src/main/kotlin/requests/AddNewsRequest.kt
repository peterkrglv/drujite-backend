package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddNewsRequest(
    val sessionId: Int,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
)
