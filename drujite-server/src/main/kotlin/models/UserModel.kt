package models

import java.util.UUID

data class UserModel(
    val id: UUID,
    val username: String,
    val phone: String,
    val password: String,
    val gender: String,
    val isAdmin: Boolean = false,
)
