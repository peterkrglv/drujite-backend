package responses

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val id: Int,
    val name: String,
    val story: String,
    val clanId: Int,
    val imageUrl: String?,
)
