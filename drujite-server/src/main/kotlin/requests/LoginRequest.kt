package requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val password: String,
    val phone: String,
)
