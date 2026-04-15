package ru.drujite.responces

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val username: String,
    val phone: String,
)
