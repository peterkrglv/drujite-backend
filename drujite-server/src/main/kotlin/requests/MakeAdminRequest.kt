package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class MakeAdminRequest(
    val phone: String,
)
