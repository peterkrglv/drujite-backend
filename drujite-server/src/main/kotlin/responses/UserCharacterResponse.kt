package responses

import kotlinx.serialization.Serializable

@Serializable
data class UserCharacterResponse(
    val id: Int,
    val name: String,
    val player: String,
    val story: String,
    val clan: String,
    val imageUrl: String?,
)
