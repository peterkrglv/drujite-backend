package requests

import kotlinx.serialization.Serializable

@Serializable
data class IdRequest(
    val id: Int,
)
