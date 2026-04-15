package responses

import kotlinx.serialization.Serializable

@Serializable
data class ClanResponse(
    val id: Int,
    val name: String,
    val description: String? = null,
)
