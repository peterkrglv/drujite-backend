package models

import java.util.UUID

data class UserModel(
    val gender: String,
    val id: UUID,
    val password: String,
    val phone: String,
    val username: String,
    val isAdmin: Boolean = false,
)
