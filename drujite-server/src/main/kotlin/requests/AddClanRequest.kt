package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddClanRequest(
    val name: String,
    val description: String? = null,
)
