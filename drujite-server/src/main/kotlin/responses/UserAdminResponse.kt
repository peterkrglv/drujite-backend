package ru.drujite.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserAdminResponse(
    val isAdmin: Boolean,
    val phone: String,
    val username: String,
)
