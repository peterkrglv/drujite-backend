package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val username: String,
    val phone: String,
    val password: String,
    val gender: String,
)
