package ru.drujite.responces

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val phone: String,
    val username: String,
)
