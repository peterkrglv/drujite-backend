package responses

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val clanId: Int,
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val story: String,
)
