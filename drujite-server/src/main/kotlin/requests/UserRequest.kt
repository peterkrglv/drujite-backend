package requests

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val token: String,
)
