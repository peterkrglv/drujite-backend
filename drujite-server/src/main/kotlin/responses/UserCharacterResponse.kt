package responses

import kotlinx.serialization.Serializable

@Serializable
data class UserCharacterResponse(
    val clan: String,
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val player: String,
    val story: String,
)
