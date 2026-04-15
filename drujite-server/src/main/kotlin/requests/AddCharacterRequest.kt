package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddCharacterRequest(
    val clanId: Int,
    val name: String,
    val story: String,
    val image: String? = null,
)
