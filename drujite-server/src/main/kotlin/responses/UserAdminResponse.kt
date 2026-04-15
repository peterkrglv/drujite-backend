package ru.drujite.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserAdminResponse(
    val phone: String,
    val username: String,
    val isAdmin: Boolean,
)
