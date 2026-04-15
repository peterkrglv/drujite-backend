package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddCharacterRequest(
    val name: String,
    val story: String,
    val clanId: Int,
    val image: String? = null,
)
