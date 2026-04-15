package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val gender: String,
    val password: String,
    val phone: String,
    val username: String,
)
